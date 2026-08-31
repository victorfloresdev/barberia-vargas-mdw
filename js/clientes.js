document.addEventListener('DOMContentLoaded', function () {
    const btnGuardar = document.getElementById('btnGuardar');
    const formCliente = document.getElementById('formCliente');
    const modalElement = document.getElementById('modalNuevoCliente');

    if (btnGuardar) {
        btnGuardar.addEventListener('click', function () {
            const boton = this;

            boton.innerHTML = 'Guardando cliente...';
            boton.disabled = true;

            setTimeout(function () {
                const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
                modal.hide();

                if (formCliente) formCliente.reset();
                boton.innerHTML = 'Guardar Cliente';
                boton.disabled = false;
            }, 2500);
        });
    }
});