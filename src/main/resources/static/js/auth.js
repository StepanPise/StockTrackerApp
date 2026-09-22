function checkAuthState() {
    const token = localStorage.getItem('jwt_token');

    if (token) {
        document.getElementById('auth-section').classList.add('hidden');
        document.getElementById('dashboard-section').classList.remove('hidden');

        const payload = parseJwt(token);

        if (payload && payload.sub) {
            document.getElementById('user-email').textContent = payload.sub;
        }

        fetchAlerts();
        fetchWebhooks();
        fetchStocks();
    } else {
        document.getElementById('auth-section').classList.remove('hidden');
        document.getElementById('dashboard-section').classList.add('hidden');
    }
}

async function auth(action) {
    const email = document.getElementById('authEmail').value.trim();
    const password = document.getElementById('authPassword').value;

    if (!email || !password) {
        showMsg('auth-msg', 'Please fill in both email and password.', true);
        return;
    }
    if (!email.includes('@')) {
        showMsg('auth-msg', 'Please enter a valid email address.', true);
        return;
    }

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