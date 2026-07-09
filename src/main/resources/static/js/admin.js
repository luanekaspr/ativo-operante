// Funções do Administrador

async function carregarDashboardAdmin() {
    try {
        showLoading(true);

        await Promise.all([
            carregarTodasDenuncias(),
            carregarOrgaosAdmin(),
            carregarTiposAdmin(),
            carregarEstatisticas()
        ]);

        // Configurar eventos
        document.getElementById('formOrgao')?.addEventListener('submit', adicionarOrgao);
        document.getElementById('formTipo')?.addEventListener('submit', adicionarTipo);

        mostrarSecaoAdmin('denuncias');

    } catch (error) {
        showAlert('Erro ao carregar dashboard: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

function mostrarSecaoAdmin(secao) {
    const secoes = ['denuncias', 'orgaos', 'tipos'];
    secoes.forEach(s => {
        const elem = document.getElementById(`secaoAdmin${s.charAt(0).toUpperCase() + s.slice(1)}`);
        if (elem) elem.classList.add('hidden');
    });

    const secaoElem = document.getElementById(`secaoAdmin${secao.charAt(0).toUpperCase() + secao.slice(1)}`);
    if (secaoElem) secaoElem.classList.remove('hidden');

    if (secao === 'denuncias') carregarTodasDenuncias();
    else if (secao === 'orgaos') carregarOrgaosAdmin();
    else if (secao === 'tipos') carregarTiposAdmin();
}

async function carregarEstatisticas() {
    try {
        const denuncias = await apiRequest('/apis/adm/denuncias-all', 'GET', null, true);
        const statsGrid = document.getElementById('statsGrid');

        if (statsGrid && denuncias) {
            const total = denuncias.length;
            const urgentes = denuncias.filter(d => d.urgencia >= 4).length;
            const respondidas = denuncias.filter(d => d.feedback).length;

            statsGrid.innerHTML = `
                <div class="stat-card">
                    <i class="fas fa-exclamation-triangle"></i>
                    <div class="number">${total}</div>
                    <div class="label">Total de Denúncias</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-fire"></i>
                    <div class="number">${urgentes}</div>
                    <div class="label">Denúncias Urgentes</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-check-circle"></i>
                    <div class="number">${respondidas}</div>
                    <div class="label">Denúncias Respondidas</div>
                </div>
            `;
        }
    } catch (error) {
        console.error('Erro ao carregar estatísticas:', error);
    }
}

async function carregarTodasDenuncias() {
    try {
        showLoading(true);

        const denuncias = await apiRequest('/apis/adm/denuncias-all', 'GET', null, true);
        const container = document.getElementById('todasDenunciasList');

        if (!container) return;

        if (!denuncias || denuncias.length === 0) {
            container.innerHTML = `<div class="empty-state"><i class="fas fa-inbox"></i><p>Nenhuma denúncia encontrada</p></div>`;
            return;
        }

        container.innerHTML = '<div class="denuncias-grid">';
        denuncias.forEach(denuncia => {
            container.innerHTML += `
                <div class="denuncia-card">
                    <span class="urgencia urgencia-${denuncia.urgencia}">Urgência ${denuncia.urgencia}</span>
                    <div class="denuncia-titulo">${denuncia.titulo}</div>
                    <div class="denuncia-descricao">${denuncia.texto}</div>
                    <div class="denuncia-meta">
                        <div><strong>Data:</strong> ${new Date(denuncia.dataHora).toLocaleDateString()}</div>
                        <div><strong>Cidadão:</strong> ${denuncia.usuario?.email || 'N/A'}</div>
                        <div><strong>Órgão:</strong> ${denuncia.orgao?.nome || 'N/A'}</div>
                    </div>
                    ${denuncia.foto ? `
                        <div class="denuncia-foto">
                            <img src="http://localhost:8080/uploads/${denuncia.foto}"
                                 style="max-width:100%; max-height:150px; object-fit:cover; border-radius:8px; margin-top:0.5rem; cursor:pointer;"
                                 onclick="window.open('http://localhost:8080/uploads/${denuncia.foto}', '_blank')">
                        </div>
                    ` : ''}
                    ${denuncia.feedback ? `<div class="feedback"><strong>Feedback:</strong> ${denuncia.feedback.texto}</div>` : ''}
                    <div style="margin-top: 1rem; display: flex; gap: 0.5rem;">
                        <button class="btn-info" onclick="abrirFeedbackModal(${denuncia.id})">Dar Feedback</button>
                        <button class="btn-danger" onclick="excluirDenuncia(${denuncia.id})">Excluir</button>
                    </div>
                </div>
            `;
        });
        container.innerHTML += '</div>';

    } catch (error) {
        console.error('Erro ao carregar denúncias:', error);
    } finally {
        showLoading(false);
    }
}

function abrirFeedbackModal(denunciaId) {
    const modal = document.createElement('div');
    modal.id = 'feedbackModal';
    modal.className = 'modal';
    modal.style.display = 'flex';
    modal.innerHTML = `
        <div class="modal-content">
            <h3>Dar Feedback</h3>
            <form id="formFeedback">
                <input type="hidden" id="feedbackDenunciaId" value="${denunciaId}">
                <div class="form-group">
                    <label>Feedback</label>
                    <textarea id="feedbackTexto" required rows="4"></textarea>
                </div>
                <div class="modal-buttons">
                    <button type="submit" class="btn-primary">Enviar</button>
                    <button type="button" class="btn-secondary" onclick="this.closest('.modal').remove()">Cancelar</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(modal);

    document.getElementById('formFeedback').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('feedbackDenunciaId').value;
        const texto = document.getElementById('feedbackTexto').value;

        try {
            await apiRequest(`/apis/adm/denuncias/${id}/feedback`, 'POST', texto, true);
            showAlert('Feedback enviado com sucesso!', 'success');
            modal.remove();
            await carregarTodasDenuncias();
        } catch (error) {
            showAlert(error.message, 'error');
        }
    });
}

async function excluirDenuncia(id) {
    if (confirm('Tem certeza que deseja excluir esta denúncia?')) {
        try {
            await apiRequest(`/apis/adm/denuncia/deletar/${id}`, 'DELETE', null, true);
            showAlert('Denúncia excluída!', 'success');
            await carregarTodasDenuncias();
            await carregarEstatisticas();
        } catch (error) {
            showAlert(error.message, 'error');
        }
    }
}

async function carregarOrgaosAdmin() {
    try {
        const orgaos = await apiRequest('/apis/adm/orgao-all', 'GET', null, true);
        const container = document.getElementById('orgaosList');

        if (!container) return;

        if (!orgaos || orgaos.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>Nenhum órgão cadastrado</p></div>';
            return;
        }

        container.innerHTML = '<div class="denuncias-grid">';
        orgaos.forEach(orgao => {
            container.innerHTML += `
                <div class="denuncia-card">
                    <div class="denuncia-titulo">${orgao.nome}</div>
                    <button class="btn-danger" onclick="excluirOrgao(${orgao.id})">Excluir</button>
                </div>
            `;
        });
        container.innerHTML += '</div>';

    } catch (error) {
        console.error('Erro ao carregar órgãos:', error);
    }
}

async function adicionarOrgao(e) {
    e.preventDefault();
    const nome = document.getElementById('orgaoNome').value;

    try {
        await apiRequest('/apis/adm/orgao', 'POST', { nome }, true);
        showAlert('Órgão adicionado!', 'success');
        document.getElementById('orgaoNome').value = '';
        await carregarOrgaosAdmin();
    } catch (error) {
        showAlert(error.message, 'error');
    }
}

async function excluirOrgao(id) {
    if (confirm('Tem certeza?')) {
        try {
            await apiRequest(`/apis/adm/orgao/deletar/${id}`, 'DELETE', null, true);
            showAlert('Órgão excluído!', 'success');
            await carregarOrgaosAdmin();
        } catch (error) {
            showAlert(error.message, 'error');
        }
    }
}

async function carregarTiposAdmin() {
    try {
        const tipos = await apiRequest('/apis/adm/tipos-all', 'GET', null, true);
        const container = document.getElementById('tiposList');

        if (!container) return;

        if (!tipos || tipos.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>Nenhum tipo cadastrado</p></div>';
            return;
        }

        container.innerHTML = '<div class="denuncias-grid">';
        tipos.forEach(tipo => {
            container.innerHTML += `
                <div class="denuncia-card">
                    <div class="denuncia-titulo">${tipo.nome}</div>
                    <button class="btn-danger" onclick="excluirTipo(${tipo.id})">Excluir</button>
                </div>
            `;
        });
        container.innerHTML += '</div>';

    } catch (error) {
        console.error('Erro ao carregar tipos:', error);
    }
}

async function adicionarTipo(e) {
    e.preventDefault();
    const nome = document.getElementById('tipoNome').value;

    try {
        await apiRequest('/apis/adm/tipos', 'POST', { nome }, true);
        showAlert('Tipo adicionado!', 'success');
        document.getElementById('tipoNome').value = '';
        await carregarTiposAdmin();
    } catch (error) {
        showAlert(error.message, 'error');
    }
}

async function excluirTipo(id) {
    if (confirm('Tem certeza?')) {
        try {
            await apiRequest(`/apis/adm/tipos/deletar/${id}`, 'DELETE', null, true);
            showAlert('Tipo excluído!', 'success');
            await carregarTiposAdmin();
        } catch (error) {
            showAlert(error.message, 'error');
        }
    }
}

// Tornar funções globais
window.mostrarSecaoAdmin = mostrarSecaoAdmin;
window.abrirFeedbackModal = abrirFeedbackModal;
window.excluirDenuncia = excluirDenuncia;
window.excluirOrgao = excluirOrgao;
window.excluirTipo = excluirTipo;