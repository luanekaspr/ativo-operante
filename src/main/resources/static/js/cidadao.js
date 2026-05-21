// Funções do Cidadão

async function carregarDashboardCidadao() {
    try {
        showLoading(true);
        await Promise.all([
            carregarOrgaos(),
            carregarTipos(),
            carregarMinhasDenuncias()
        ]);

        // Configurar eventos
        document.getElementById('formDenuncia')?.addEventListener('submit', enviarDenuncia);

        mostrarSecao('novaDenuncia');

    } catch (error) {
        showAlert('Erro ao carregar dashboard: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

function mostrarSecao(secao) {
    const secoes = ['novaDenuncia', 'minhasDenuncias'];
    secoes.forEach(s => {
        const elem = document.getElementById(`secao${s.charAt(0).toUpperCase() + s.slice(1)}`);
        if (elem) elem.classList.add('hidden');
    });

    const secaoElem = document.getElementById(`secao${secao.charAt(0).toUpperCase() + secao.slice(1)}`);
    if (secaoElem) secaoElem.classList.remove('hidden');

    if (secao === 'minhasDenuncias') {
        carregarMinhasDenuncias();
    }
}

async function carregarOrgaos() {
    try {
        const orgaos = await apiRequest('/apis/cidadao/orgao-all', 'GET', null, true);
        const select = document.getElementById('denunciaOrgao');
        if (select && orgaos) {
            select.innerHTML = '<option value="">Selecione um órgão</option>';
            orgaos.forEach(orgao => {
                select.innerHTML += `<option value="${orgao.id}">${orgao.nome}</option>`;
            });
        }
    } catch (error) {
        console.error('Erro ao carregar órgãos:', error);
    }
}

async function carregarTipos() {
    try {
        const tipos = await apiRequest('/apis/cidadao/tipos-all', 'GET', null, true);
        const select = document.getElementById('denunciaTipo');
        if (select && tipos) {
            select.innerHTML = '<option value="">Selecione um tipo</option>';
            tipos.forEach(tipo => {
                select.innerHTML += `<option value="${tipo.id}">${tipo.nome}</option>`;
            });
        }
    } catch (error) {
        console.error('Erro ao carregar tipos:', error);
    }
}

async function enviarDenuncia(e) {
    e.preventDefault();

    try {
        showLoading(true);

        const tokenInfo = decodeToken(authToken);
        let usuarioId = null;

        if (tokenInfo && tokenInfo.id) {
            usuarioId = parseInt(tokenInfo.id); // lê o claim "id", não o sub
        }

        if (!usuarioId) {
            showAlert('Erro: Não foi possível identificar o usuário.', 'error');
            return;
        }

        const denuncia = {
            titulo: document.getElementById('denunciaTitulo').value,
            descricao: document.getElementById('denunciaDescricao').value,
            data: document.getElementById('denunciaData').value,
            urgencia: parseInt(document.getElementById('denunciaUrgencia').value),
            orgao: { id: parseInt(document.getElementById('denunciaOrgao').value) },
            tipo: { id: parseInt(document.getElementById('denunciaTipo').value) },
            usuario: { id: usuarioId }
        };

        await apiRequest('/apis/cidadao/denuncias', 'POST', denuncia, true);

        showAlert('Denúncia enviada com sucesso!', 'success');
        document.getElementById('formDenuncia').reset();
        document.getElementById('denunciaUrgencia').value = '3';
        document.getElementById('urgenciaValue').textContent = '3';

    } catch (error) {
        showAlert(error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function carregarMinhasDenuncias() {
    try {
        showLoading(true);

        const denuncias = await apiRequest('/apis/cidadao/denuncias/usuario/minhas', 'GET', null, true);
        const container = document.getElementById('minhasDenunciasList');

        if (!container) return;

        if (!denuncias || denuncias.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <p>Você ainda não fez nenhuma denúncia</p>
                </div>
            `;
            return;
        }

        container.innerHTML = '<div class="denuncias-grid">';
        denuncias.forEach(denuncia => {
            container.innerHTML += `
                <div class="denuncia-card">
                    <span class="urgencia urgencia-${denuncia.urgencia}">Urgência ${denuncia.urgencia}</span>
                    <div class="denuncia-titulo">${denuncia.titulo}</div>
                    <div class="denuncia-descricao">${denuncia.descricao}</div>
                    <div class="denuncia-meta">
                        <div><strong>Data:</strong> ${new Date(denuncia.data).toLocaleDateString()}</div>
                        <div><strong>Órgão:</strong> ${denuncia.orgao?.nome || 'N/A'}</div>
                        <div><strong>Tipo:</strong> ${denuncia.tipo?.nome || 'N/A'}</div>
                    </div>
                    ${denuncia.feedback ? `
                        <div class="feedback">
                            <strong><i class="fas fa-comment"></i> Feedback:</strong>
                            <p>${denuncia.feedback.texto}</p>
                            <small>${denuncia.feedback.data ? new Date(denuncia.feedback.data).toLocaleString() : ''}</small>
                        </div>
                    ` : ''}
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