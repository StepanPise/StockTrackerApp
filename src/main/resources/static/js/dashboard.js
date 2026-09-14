// --- ALERTS LOGIC ---
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
            syncDashboardHeight();
            return;
        }

        alerts.sort((a, b) => {
            const aActive = a.active ?? a.isActive ?? false;
            const bActive = b.active ?? b.isActive ?? false;
            return (bActive === aActive) ? 0 : bActive ? 1 : -1;
        });

        alerts.forEach(alert => {
            const isActive = alert.active ?? alert.isActive ?? false;

            const logoHtml = alert.logoUrl
                ? `<img src="${alert.logoUrl}" alt="${alert.stockTicker}" class="stock-logo" onerror="this.style.display='none'">`
                : `<div class="stock-logo-placeholder">${alert.stockTicker.slice(0, 3)}</div>`;

            const displayName = alert.stockName
                ? `${alert.stockName} (${alert.stockTicker})`
                : alert.stockTicker;

            const statusBadge = !isActive ? '<span class="badge-triggered">Triggered</span>' : '';
            const itemClasses = isActive ? 'alert-item' : 'alert-item inactive';

            listEl.innerHTML += `
                <div class="${itemClasses}" id="alert-${alert.id}">
                    <div class="alert-left">
                        ${logoHtml}
                        <div class="alert-info">
                            <h4>${displayName} ${statusBadge}</h4>
                            <p>Condition: Price goes <b>${alert.conditionType}</b> ${alert.targetPrice} USD</p>
                        </div>
                    </div>
                    <button class="btn btn-red" onclick="deleteAlert(${alert.id})">Delete</button>
                </div>
            `;
        });

        syncDashboardHeight();
    } catch (error) {
        listEl.innerHTML = '<p class="error">Failed to load alerts.</p>';
        syncDashboardHeight();
    }
}

async function createAlert() {
    const token = localStorage.getItem('jwt_token');
    const tickerValue = document.getElementById('alertTicker').value.trim().toUpperCase();
    const priceValue = parseFloat(document.getElementById('alertPrice').value);
    const conditionValue = document.getElementById('alertCondition').value;

    if (!tickerValue) {
        showMsg('alert-msg', 'Stock ticker cannot be empty.', true);
        return;
    }
    if (isNaN(priceValue) || priceValue <= 0) {
        showMsg('alert-msg', 'Target price must be a valid number greater than 0.', true);
        return;
    }

    const payload = {
        name: `Alert ${tickerValue}`,
        ticker: tickerValue,
        targetPrice: priceValue,
        conditionType: conditionValue
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
    const token = localStorage.getItem('jwt_token');

    try {
        const response = await fetch(`${API_URL}/alerts/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            document.getElementById(`alert-${id}`).remove();
            syncDashboardHeight();
        } else {
            alert('Error deleting alert.');
        }
    } catch (error) {
        alert('Connection error.');
    }
}

// --- STOCKS LOGIC ---
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

// --- NOTIFICATIONS LOGIC ---
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

    setTimeout(syncDashboardHeight, 50);
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
    const type = document.getElementById('notifType').value;
    const webhookUrl = document.getElementById('notifWebhook').value.trim();
    const email = document.getElementById('notifEmail').value.trim();

    if (type === 'WEBHOOK' && (!webhookUrl || !webhookUrl.startsWith('http'))) {
        showMsg('notif-msg', 'Please enter a valid Webhook URL starting with http/https.', true);
        return;
    }
    if (type === 'EMAIL' && (!email || !email.includes('@'))) {
        showMsg('notif-msg', 'Please enter a valid email address.', true);
        return;
    }

    const payload = { type, webhookUrl, email };

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