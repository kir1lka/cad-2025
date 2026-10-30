const deleteDialog = document.getElementById('delete-dialog');
const deleteTitle = document.getElementById('delete-title');
let selectedForm = null;

document.querySelectorAll('[data-delete-form]').forEach(form => {
    form.addEventListener('submit', event => {
        event.preventDefault();
        selectedForm = form;
        deleteTitle.textContent = form.dataset.taskTitle;
        deleteDialog.returnValue = '';
        deleteDialog.showModal();
    });
});

deleteDialog.addEventListener('close', () => {
    if (deleteDialog.returnValue === 'delete' && selectedForm) {
        selectedForm.submit();
    }
    selectedForm = null;
});
