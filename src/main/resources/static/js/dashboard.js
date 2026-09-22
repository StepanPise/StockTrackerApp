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

            const statusBadge = !isActive
                ? '<span class="badge-triggered">Triggered</span>'
                : '';

            const itemClasses = isActive
                ? 'alert-item'
                : 'alert-item inactive';

            listEl.innerHTML += `
                <div class="${itemClasses}" id="alert-${alert.id}">
                    <div class="alert-left">
                        ${logoHtml}

                        <div class="alert-info">
                            <h4>${displayName} ${statusBadge}</h4>
                            <p>
                                Condition: Price goes
                                <b>${alert.conditionType}</b>
                                ${alert.targetPrice} USD
                            </p>
                        </div>
                    </div>

                    <button
                        class="btn btn-red"
                        onclick="deleteAlert(${alert.id})">
                        Delete
                    </button>
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

    const tickerValue =
        document.getElementById('alertTicker').value.trim().toUpperCase();

    const priceValue =
        parseFloat(document.getElementById('alertPrice').value);

    const conditionValue =
        document.getElementById('alertCondition').value;

    if (!tickerValue) {
        showMsg(
            'alert-msg',
            'Stock ticker cannot be empty.',
            true
        );
        return;
    }

    if (isNaN(priceValue) || priceValue <= 0) {
        showMsg(
            'alert-msg',
            'Target price must be a valid number greater than 0.',
            true
        );
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
            showMsg(
                'alert-msg',
                'Alert successfully created!'
            );

            fetchAlerts();
            fetchStocks();

            document.getElementById('alertTicker').value = '';
            document.getElementById('alertPrice').value = '';

        } else {
            const errorData =
                await response.json().catch(() => ({}));

            console.error(
                'Backend error detail:',
                errorData
            );

            showMsg(
                'alert-msg',
                'Failed to create alert. Check console.',
                true
            );
        }

    } catch (error) {
        showMsg(
            'alert-msg',
            'Server error.',
            true
        );
    }
}


async function deleteAlert(id) {
    const token = localStorage.getItem('jwt_token');

    try {
        const response = await fetch(
            `${API_URL}/alerts/${id}`,
            {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
        );

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
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const stocks = await response.json();

            datalist.innerHTML = '';

            stocks.forEach(stock => {
                const option = document.createElement('option');

                option.value = stock.ticker;
                option.textContent = stock.name
                    ? `${stock.ticker} - ${stock.name}`
                    : stock.ticker;

                datalist.appendChild(option);
            });
        }

    } catch (error) {
        console.error(
            'Failed to fetch stocks for datalist:',
            error
        );
    }
}


// --- WEBHOOKS LOGIC ---

let currentWebhooks = [];


async function fetchWebhooks() {
    const token = localStorage.getItem('jwt_token');

    try {
        const response = await fetch(`${API_URL}/webhooks`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 403 || response.status === 401) {
            logout();
            return;
        }

        if (!response.ok) {
            console.error('Failed to load webhooks.');
            return;
        }

        const webhooks = await response.json();

        // Store them so editWebhook() can find the selected webhook.
        currentWebhooks = webhooks;

        renderWebhooks(webhooks);

    } catch (error) {
        console.error(
            'Failed to load webhooks.',
            error
        );
    }
}


function renderWebhooks(webhooks) {
    const container =
        document.getElementById('webhooks-list');

    if (!container) return;

    container.innerHTML = '';

    if (webhooks.length === 0) {
        container.innerHTML = `
            <p class="webhooks-empty">
                No webhooks configured.
            </p>
        `;
        return;
    }

    webhooks.forEach(webhook => {
        const webhookElement =
            document.createElement('div');

        webhookElement.className = 'webhook-item';

        webhookElement.innerHTML = `
            <div class="webhook-info">
                <strong>
                    ${escapeHtml(webhook.name)}
                </strong>

                <span>
                    ${escapeHtml(webhook.type)}
                </span>
            </div>

            <div class="webhook-actions">
                <button
                    class="btn btn-blue"
                    onclick="editWebhook(${webhook.id})">
                    Edit
                </button>

                <button
                    class="btn btn-red"
                    onclick="deleteWebhook(${webhook.id})">
                    Delete
                </button>
            </div>
        `;

        container.appendChild(webhookElement);
    });

    syncDashboardHeight();
}


async function addWebhook() {
    const token = localStorage.getItem('jwt_token');

    const name =
        document.getElementById('webhookName').value.trim();

    const type =
        document.getElementById('webhookType').value;

    const url =
        document.getElementById('webhookUrl').value.trim();

    if (!name || !url) {
        showMsg(
            'webhook-msg',
            'Please fill in all fields.',
            true
        );
        return;
    }

    if (
        !url.startsWith('http://') &&
        !url.startsWith('https://')
    ) {
        showMsg(
            'webhook-msg',
            'Please enter a valid Webhook URL.',
            true
        );
        return;
    }

    const payload = {
        name,
        type,
        url
    };

    try {
        const response = await fetch(
            `${API_URL}/webhooks`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(payload)
            }
        );

        const data =
            await response.json().catch(() => ({}));

        if (response.ok) {
            showMsg(
                'webhook-msg',
                'Webhook successfully added!'
            );

            clearWebhookForm();
            await fetchWebhooks();

        } else {
            showMsg(
                'webhook-msg',
                data.message || 'Error adding webhook.',
                true
            );
        }

    } catch (error) {
        showMsg(
            'webhook-msg',
            'Connection error.',
            true
        );
    }
}


async function updateWebhook() {
    const token = localStorage.getItem('jwt_token');

    const id =
        document.getElementById('webhookId').value;

    const name =
        document.getElementById('webhookName').value.trim();

    const type =
        document.getElementById('webhookType').value;

    const url =
        document.getElementById('webhookUrl').value.trim();

    if (!id || !name || !url) {
        showMsg(
            'webhook-msg',
            'Please fill in all fields.',
            true
        );
        return;
    }

    if (
        !url.startsWith('http://') &&
        !url.startsWith('https://')
    ) {
        showMsg(
            'webhook-msg',
            'Please enter a valid Webhook URL.',
            true
        );
        return;
    }

    const payload = {
        name,
        type,
        url
    };

    try {
        const response = await fetch(
            `${API_URL}/webhooks/${id}`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(payload)
            }
        );

        const data =
            await response.json().catch(() => ({}));

        if (response.ok) {
            showMsg(
                'webhook-msg',
                'Webhook successfully updated!'
            );

            clearWebhookForm();
            await fetchWebhooks();

        } else {
            showMsg(
                'webhook-msg',
                data.message || 'Error updating webhook.',
                true
            );
        }

    } catch (error) {
        showMsg(
            'webhook-msg',
            'Connection error.',
            true
        );
    }
}


function submitWebhook() {
    const id =
        document.getElementById('webhookId').value;

    if (id) {
        updateWebhook();
    } else {
        addWebhook();
    }
}


function editWebhook(id) {
    const webhook =
        currentWebhooks.find(webhook => webhook.id === id);

    if (!webhook) {
        return;
    }

    document.getElementById('webhookId').value =
        webhook.id;

    document.getElementById('webhookName').value =
        webhook.name;

    document.getElementById('webhookType').value =
        webhook.type;

    document.getElementById('webhookUrl').value =
        webhook.url;

    document.getElementById('webhook-submit-btn').textContent =
        'Update Webhook';

    document.getElementById('webhook-cancel-btn')
        .classList.remove('hidden');

    document.getElementById('webhookName').focus();
}


function clearWebhookForm() {
    document.getElementById('webhookId').value = '';
    document.getElementById('webhookName').value = '';
    document.getElementById('webhookType').value = 'DISCORD';
    document.getElementById('webhookUrl').value = '';

    document.getElementById('webhook-submit-btn').textContent =
        'Add Webhook';

    document.getElementById('webhook-cancel-btn')
        .classList.add('hidden');
}


function cancelWebhookEdit() {
    clearWebhookForm();
}


async function deleteWebhook(id) {
    const token = localStorage.getItem('jwt_token');

    if (!confirm(
        'Are you sure you want to delete this webhook?'
    )) {
        return;
    }

    try {
        const response = await fetch(
            `${API_URL}/webhooks/${id}`,
            {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
        );

        if (response.ok) {
            showMsg(
                'webhook-msg',
                'Webhook successfully deleted!'
            );

            await fetchWebhooks();

        } else {
            const data =
                await response.json().catch(() => ({}));

            showMsg(
                'webhook-msg',
                data.message || 'Error deleting webhook.',
                true
            );
        }

    } catch (error) {
        showMsg(
            'webhook-msg',
            'Connection error.',
            true
        );
    }
}


function escapeHtml(value) {
    const div = document.createElement('div');
    div.textContent = value;
    return div.innerHTML;
}