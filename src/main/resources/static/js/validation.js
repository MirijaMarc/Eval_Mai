$(document).ready(function () {
    $('#formPaiement').submit(function (event) {
        event.preventDefault(); // Empêche la soumission du formulaire par défaut
        
        var montant = $('#montant').val();
        var id = $('#iddevis').val();
        $.ajax({
            type: 'POST',
            url: '/client/validerMontant',
            data: {
                montant: montant,
                id: id
            },
            success: function (response) {
                if (response !== 'OK') {
                    Toastify({
                        text: response,
                
                        duration: 10000,
                        close: true,
                        style: {
                            background: "#dc3545",
                        }
                
                        }).showToast();
                } else {
                    $('#formPaiement').unbind('submit').submit(); // Soumet le formulaire
                }
            },
            error: function () {
                console.log('Erreur lors de la validation AJAX.');
            }
        });
    });
});