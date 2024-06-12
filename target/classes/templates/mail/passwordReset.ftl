<#import "layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
    <div>
        <table align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
            <tr>
                <td style="padding: 20px 30px;">
                    <p>Bonjour Cher(e) Utilisateur(rice),</p>
                    <p>Vous avez demandé à réinitialiser votre mot de passe. Veuillez suivre le lien ci-dessous pour définir un nouveau mot de passe :</p>
                    <p><a href="${msg}" target="_blank">Réinitialiser le mot de passe</a></p>
                </td>
            </tr>
            <tr>
                <td style="padding: 30px;">
                    <p>
                        Si vous n'avez pas demandé cette action, veuillez ignorer cet e-mail ou contacter le support si vous pensez qu'une erreur s'est produite.
                    </p>
                    <p>
                        Nous vous remercions de votre confiance.
                        <br><br>
                        Cordialement,
                        <br>
                        L'équipe de MAE Assurances
                    </p>
                </td>
            </tr>
        </table>
    </div>
</@layout.myLayout>
