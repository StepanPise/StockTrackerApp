document.addEventListener('DOMContentLoaded', () => {
    checkAuthState();
    window.addEventListener('resize', syncDashboardHeight);
});