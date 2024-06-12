<#import "layout/defaultLayout.ftl" as layout>
<@layout.myLayout>
	<div>
		<table align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
			<tr>
				<td style="padding: 20px 30px;">
					<p>Bonjour Cher(e) Utilisateur(rice),</p>
					<p>Merci de vous être inscrit(e) sur notre plateforme. Veuillez confirmer votre adresse e-mail en suivant le lien ci-dessous :</p>
					<p><a href="${msg}" target="_blank">Confirmer l'adresse e-mail</a></p>
				</td>
			</tr>
			<tr>
				<td style="padding: 30px;">
					<p>
						Si vous rencontrez des problèmes pour cliquer sur le lien de confirmation, copiez et collez l'URL suivante dans votre navigateur web.
						<br>
						${msg}
					</p>
					<p>
						Pour toute question, n'hésitez pas à contacter notre support.
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
