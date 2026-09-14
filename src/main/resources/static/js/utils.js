const API_URL = '/api';

function showMsg(elementId, text, isError = false) {
    const el = document.getElementById(elementId);
    el.textContent = text;
    el.className = 'msg ' + (isError ? 'error' : 'success');
    setTimeout(() => el.textContent = '', 3000);
}

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

function syncDashboardHeight() {
    const sidebar = document.querySelector('.sidebar');
    const alertsBox = document.querySelector('.alerts-box');
    if (sidebar && alertsBox) {
        alertsBox.style.height = `${sidebar.offsetHeight}px`;
    }
}