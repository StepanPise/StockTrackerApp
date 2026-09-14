const API_URL = '/api';

//  INITIALIZATION
document.addEventListener('DOMContentLoaded', () => {
    checkAuthState();
});

function checkAuthState() {
    const token = localStorage.getItem('jwt_token');
    if (token) {
        document.getElementById('auth-section').classList.add('hidden');
        document.getElementById('dashboard-section').classList.remove('hidden');
        fetchAlerts();
        fetchNotificationSettings();
        fetchStocks();
    } else {
        document.getElementById('auth-section').classList.remove('hidden');
        document.getElementById('dashboard-section').classList.add('hidden');
    }
}

function showMsg(elementId, text, isError = false) {
    const el = document.getElementById(elementId);
    el.textContent = text;
    el.className = 'msg ' + (isError ? 'error' : 'success');
    setTimeout(() => el.textContent = '', 3000);
}

//  AUTH LOGIC
async function auth(action) {
    const email = document.getElementById('authEmail').value;
    const password = document.getElementById('authPassword').value;

    try {
        const response = await fetch(`${API_URL}/auth/${action}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem('jwt_token', data.token);
            checkAuthState();
        } else {
            showMsg('auth-msg', `Error: ${data.message || response.statusText}`, true);
        }
    } catch (error) {
        showMsg('auth-msg', 'Connection error.', true);
    }
}

const login = () => auth('login');
const register = () => auth('register');

function logout() {
    localStorage.removeItem('jwt_token');
    checkAuthState();
}

//ALERTS LOGIC
async function fetchAlerts() {
    const token = localStorage.getItem('jwt_token');
    const listEl = document.getElementById('alerts-list');

    try {
        const response = await fetch(`${API_URL}/alerts/me`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 403 || response.status === 401) {
            logout();
            return;
        }

        const alerts = await response.json();
        listEl.innerHTML = '';

        if (alerts.length === 0) {
            listEl.innerHTML = '<p>You are not tracking any stocks yet.</p>';
            return;
        }

        alerts.forEach(alert => {
            listEl.innerHTML += `
                <div class="alert-item" id="alert-${alert.id}">
                    <div class="alert-info">
                        <h4>${alert.stockTicker}</h4>
                        <p>Condition: Price goes <b>${alert.conditionType}</b> ${alert.targetPrice} USD</p>
                    </div>
                    <button class="btn btn-red" onclick="deleteAlert(${alert.id})">Delete</button>
                </div>
            `;
        });
    } catch (error) {
        listEl.innerHTML = '<p class="error">Failed to load alerts.</p>';
    }
}

async function createAlert() {
    const token = localStorage.getItem('jwt_token');
    const tickerValue = document.getElementById('alertTicker').value.trim().toUpperCase();

    const payload = {
        name: `Alert ${tickerValue}`,
        ticker: tickerValue,
        targetPrice: parseFloat(document.getElementById('alertPrice').value),
        conditionType: document.getElementById('alertCondition').value
    };

    try {
        const response = await fetch(`${API_URL}/alerts`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            showMsg('alert-msg', 'Alert successfully created!');
            fetchAlerts();
            fetchStocks();

            document.getElementById('alertTicker').value = '';
            document.getElementById('alertPrice').value = '';
        } else {
            const errorData = await response.json().catch(() => ({}));
            console.error("Backend error detail:", errorData);
            showMsg('alert-msg', 'Failed to create alert. Check console.', true);
        }
    } catch (error) {
        showMsg('alert-msg', 'Server error.', true);
    }
}

async function deleteAlert(id) {
    if (!confirm('Are you sure you want to delete this alert?')) return;

    const token = localStorage.getItem('jwt_token');

    try {
        const response = await fetch(`${API_URL}/alerts/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            document.getElementById(`alert-${id}`).remove();
        } else {
            alert('Error deleting alert.');
        }
    } catch (error) {
        alert('Connection error.');
    }
}

//  STOCKS DATALIST LOGIC
async function fetchStocks() {
    const token = localStorage.getItem('jwt_token');
    const datalist = document.getElementById('stockList');

    if (!datalist) return;

    try {
        const response = await fetch(`${API_URL}/stocks`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const stocks = await response.json();
            datalist.innerHTML = '';

            stocks.forEach(stock => {
                const option = document.createElement('option');
                option.value = stock.ticker;
                option.textContent = stock.name ? `${stock.ticker} - ${stock.name}` : stock.ticker;
                datalist.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Failed to fetch stocks for datalist:', error);
    }
}

//  NOTIFICATION (WEBHOOK & EMAIL) LOGIC
function toggleNotificationFields() {
    const type = document.getElementById('notifType').value;
    const webhookContainer = document.getElementById('webhookFieldContainer');
    const emailContainer = document.getElementById('emailFieldContainer');

    if (type === 'WEBHOOK') {
        webhookContainer.classList.remove('hidden');
        emailContainer.classList.add('hidden');
    } else if (type === 'EMAIL') {
        emailContainer.classList.remove('hidden');
        webhookContainer.classList.add('hidden');
    }
}

async function fetchNotificationSettings() {
    const token = localStorage.getItem('jwt_token');

    try {
        const response = await fetch(`${API_URL}/notification-settings`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const data = await response.json();

            if (data.type) {
                document.getElementById('notifType').value = data.type;
                toggleNotificationFields();
            }
            if (data.webhookUrl) document.getElementById('notifWebhook').value = data.webhookUrl;
            if (data.email) document.getElementById('notifEmail').value = data.email;
        }
    } catch (error) {
        console.error('Failed to load notification settings.', error);
    }
}

async function saveNotificationSettings() {
    const token = localStorage.getItem('jwt_token');

    const payload = {
        type: document.getElementById('notifType').value,
        webhookUrl: document.getElementById('notifWebhook').value,
        email: document.getElementById('notifEmail').value
    };

    try {
        const response = await fetch(`${API_URL}/notification-settings`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            showMsg('notif-msg', 'Notification settings successfully saved!');
        } else {
            showMsg('notif-msg', 'Error saving settings.', true);
        }
    } catch (error) {
        showMsg('notif-msg', 'Connection error.', true);
    }
}