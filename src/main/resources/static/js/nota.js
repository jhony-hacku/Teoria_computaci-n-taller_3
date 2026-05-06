const API_URL = 'http://localhost:8080/api';

let currentEstudianteId = null;
let currentNotaId = null;

// Obtener el ID del estudiante de la URL
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    currentEstudianteId = urlParams.get('id');

    if (currentEstudianteId) {
        loadEstudianteInfo();
        loadNotas();
    } else {
        alert('No se especificó un estudiante válido');
        window.location.href = 'index.html';
    }

    document.getElementById('notaForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await saveNota();
    });
});

async function loadEstudianteInfo() {
    try {
        const response = await fetch(`${API_URL}/estudiantes/${currentEstudianteId}`);
        if (response.ok) {
            const estudiante = await response.json();
            document.getElementById('estudianteInfo').innerHTML = `
                <h2>Gestión de Notas para: ${estudiante.nombre} ${estudiante.apellido}</h2>
                <p><strong>Correo:</strong> ${estudiante.correo}</p>
                <button class="button" onclick="calcularNotaFinal()">Calcular Nota Final</button>
                <button class="button" onclick="volver()" style="background: #666;">Volver</button>
            `;
        } else {
            alert('Error al cargar la información del estudiante');
        }
    } catch (error) {
        console.error('Error cargando estudiante:', error);
        alert('Error al cargar la información del estudiante');
    }
}

async function loadNotas() {
    try {
        const response = await fetch(`${API_URL}/notas/estudiante/${currentEstudianteId}`);
        const notas = await response.json();
        displayNotas(notas);
    } catch (error) {
        console.error('Error cargando notas:', error);
        alert('Error al cargar las notas');
    }
}

function displayNotas(notas) {
    const tbody = document.getElementById('notasList');
    tbody.innerHTML = '';

    if (notas.length === 0) {
        const row = tbody.insertRow();
        const cell = row.insertCell(0);
        cell.colSpan = 6;
        cell.textContent = 'No hay notas registradas para este estudiante';
        cell.style.textAlign = 'center';
        cell.style.padding = '40px';
        return;
    }

    notas.forEach(nota => {
        const row = tbody.insertRow();
        row.insertCell(0).textContent = nota.id;
        row.insertCell(1).textContent = nota.materia;
        row.insertCell(2).textContent = nota.observacion || '-';
        row.insertCell(3).textContent = nota.valor;
        row.insertCell(4).textContent = nota.porcentaje + '%';

        // Calcular contribución
        const contribucion = (nota.valor * nota.porcentaje) / 100;
        row.insertCell(5).textContent = contribucion.toFixed(2);

        const actionsCell = row.insertCell(6);
        actionsCell.className = 'action-buttons';

        const editBtn = document.createElement('button');
        editBtn.textContent = 'Editar';
        editBtn.className = 'btn-edit';
        editBtn.onclick = () => editNota(nota);

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Eliminar';
        deleteBtn.className = 'btn-delete';
        deleteBtn.onclick = () => deleteNota(nota.id);

        actionsCell.appendChild(editBtn);
        actionsCell.appendChild(deleteBtn);
    });

    // Actualizar resumen
    updateResumen(notas);
}

function updateResumen(notas) {
    const sumaPorcentajes = notas.reduce((sum, nota) => sum + nota.porcentaje, 0);
    const sumaContribuciones = notas.reduce((sum, nota) => sum + (nota.valor * nota.porcentaje) / 100, 0);
    const notaActual = sumaContribuciones;

    const resumenDiv = document.getElementById('resumen');
    const porcentajeRestante = 100 - sumaPorcentajes;

    let notaNecesaria = null;
    if (porcentajeRestante > 0 && porcentajeRestante < 100) {
        // Calcular nota necesaria en el examen final para pasar (nota mínima 3.0)
        const notaDeseada = 3.0;
        notaNecesaria = ((notaDeseada - notaActual) * 100) / porcentajeRestante;
        notaNecesaria = Math.max(0, Math.min(5, notaNecesaria));
    }

    resumenDiv.innerHTML = `
        <h3>Resumen de Calificaciones</h3>
        <p><strong>Suma de Porcentajes:</strong> ${sumaPorcentajes.toFixed(1)}%</p>
        <p><strong>Nota Acumulada Actual:</strong> ${notaActual.toFixed(2)}</p>
        <p><strong>Porcentaje Restante:</strong> ${porcentajeRestante.toFixed(1)}%</p>
        ${notaNecesaria !== null ? `<p><strong>Nota necesaria para aprobar (3.0):</strong> ${notaNecesaria.toFixed(2)}</p>` : ''}
        ${sumaPorcentajes === 100 ? `<p><strong>Nota Final:</strong> ${notaActual.toFixed(2)} - ${notaActual >= 3 ? 'APROBADO' : 'REPROBADO'}</p>` : ''}
        ${sumaPorcentajes > 100 ? '<p style="color: red;"><strong>Error:</strong> La suma de porcentajes supera el 100%</p>' : ''}
    `;
}

function showNotaForm() {
    currentNotaId = null;
    document.getElementById('formTitle').textContent = 'Agregar Nota';
    document.getElementById('notaFormContainer').classList.add('active');
    document.getElementById('notaForm').reset();
    document.getElementById('notaId').value = '';
}

function hideNotaForm() {
    document.getElementById('notaFormContainer').classList.remove('active');
}

async function saveNota() {
    const id = document.getElementById('notaId').value;
    const nota = {
        materia: document.getElementById('materia').value,
        observacion: document.getElementById('observacion').value,
        valor: parseFloat(document.getElementById('valor').value),
        porcentaje: parseFloat(document.getElementById('porcentaje').value),
        estudianteId: currentEstudianteId
    };

    // Validaciones
    if (nota.valor < 0 || nota.valor > 5) {
        alert('La nota debe estar entre 0 y 5');
        return;
    }

    if (nota.porcentaje < 0 || nota.porcentaje > 100) {
        alert('El porcentaje debe estar entre 0 y 100');
        return;
    }

    try {
        let response;
        if (id) {
            response = await fetch(`${API_URL}/notas/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(nota)
            });
        } else {
            response = await fetch(`${API_URL}/notas`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(nota)
            });
        }

        if (response.ok) {
            alert(id ? 'Nota actualizada' : 'Nota agregada');
            hideNotaForm();
            loadNotas();
        } else {
            const error = await response.json();
            alert('Error: ' + (error.message || 'Ocurrió un error'));
        }
    } catch (error) {
        console.error('Error guardando nota:', error);
        alert('Error al guardar la nota');
    }
}

function editNota(nota) {
    currentNotaId = nota.id;
    document.getElementById('formTitle').textContent = 'Editar Nota';
    document.getElementById('notaId').value = nota.id;
    document.getElementById('materia').value = nota.materia;
    document.getElementById('observacion').value = nota.observacion || '';
    document.getElementById('valor').value = nota.valor;
    document.getElementById('porcentaje').value = nota.porcentaje;
    document.getElementById('notaFormContainer').classList.add('active');
}

async function deleteNota(id) {
    if (confirm('¿Estás seguro de eliminar esta nota?')) {
        try {
            const response = await fetch(`${API_URL}/notas/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Nota eliminada');
                loadNotas();
            } else {
                alert('Error al eliminar la nota');
            }
        } catch (error) {
            console.error('Error eliminando nota:', error);
            alert('Error al eliminar la nota');
        }
    }
}

async function calcularNotaFinal() {
    try {
        const response = await fetch(`${API_URL}/estudiantes/${currentEstudianteId}/nota-final`);
        if (response.ok) {
            const data = await response.json();
            const notaFinal = data.notaFinal;

            let mensaje = '';
            let color = '';

            if (notaFinal >= 4.5) {
                mensaje = 'EXCELENTE - Felicitaciones';
                color = '#4CAF50';
            } else if (notaFinal >= 4.0) {
                mensaje = 'MUY BUENO';
                color = '#8BC34A';
            } else if (notaFinal >= 3.0) {
                mensaje = 'APROBADO';
                color = '#2196F3';
            } else if (notaFinal >= 2.0) {
                mensaje = 'REPROBADO - Puedes recuperar';
                color = '#FF9800';
            } else {
                mensaje = 'REPROBADO - Necesitas mejorar';
                color = '#f44336';
            }

            document.getElementById('notaFinalResult').innerHTML = `
                <div style="text-align: center;">
                    <h3 style="color: ${color};">Nota Final: ${notaFinal}</h3>
                    <p><strong>${mensaje}</strong></p>
                    ${notaFinal >= 3 ? '<p>✅ ¡Felicidades! Has aprobado la materia.</p>' : '<p>❌ Lo sentimos, no has alcanzado la nota mínima.</p>'}
                </div>
            `;
            document.getElementById('notaFinalModal').style.display = 'block';
        } else {
            const error = await response.json();
            alert('Error: ' + (error.message || 'No se pudo calcular la nota final'));
        }
    } catch (error) {
        console.error('Error calculando nota final:', error);
        alert('Error al calcular la nota final');
    }
}

function closeNotaFinalModal() {
    document.getElementById('notaFinalModal').style.display = 'none';
}

function volver() {
    window.location.href = 'index.html';
}

// Función para validar suma de porcentajes antes de guardar
async function validarPorcentajes(materia, porcentaje) {
    try {
        const response = await fetch(`${API_URL}/notas/estudiante/${currentEstudianteId}`);
        const notas = await response.json();

        let sumaActual = notas.reduce((sum, nota) => sum + nota.porcentaje, 0);

        if (currentNotaId) {
            const notaActual = notas.find(n => n.id === parseInt(currentNotaId));
            if (notaActual) {
                sumaActual -= notaActual.porcentaje;
            }
        }

        const nuevaSuma = sumaActual + porcentaje;

        if (nuevaSuma > 100) {
            alert(`Advertencia: La suma de porcentajes superará el 100% (Actual: ${sumaActual}% + ${porcentaje}% = ${nuevaSuma}%)`);
            return false;
        }

        return true;
    } catch (error) {
        console.error('Error validando porcentajes:', error);
        return true;
    }
}

// Agregar validación al formulario
document.addEventListener('DOMContentLoaded', () => {
    const porcentajeInput = document.getElementById('porcentaje');
    if (porcentajeInput) {
        porcentajeInput.addEventListener('change', async (e) => {
            const materia = document.getElementById('materia').value;
            const porcentaje = parseFloat(e.target.value);
            if (materia && !isNaN(porcentaje)) {
                await validarPorcentajes(materia, porcentaje);
            }
        });
    }
});