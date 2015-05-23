# MiniTwitter
# Projet - Application Répartie

Garance Vallat
Anaïs Marongiu
SI4


Pour faire tourner le projet
-------------------------------


Fonctionnalités
-------------------------------
Notre projet répond aux fonctionnalités de base suivantes :
1. Créer un compte MiniTwitter avec un login et un password
2. Se connecter/déconnecter
3. Créer un nouveau hashtag (lorsqu'on est connecté)
4. S'abonner auprès d'un hashtag (lorsqu'on est connecté)
5. Ecrire sur un hashtag (lorsqu'on est connecté)
6. Lire les messages du topic (lorsqu'on est connecté)
7. Voir la liste des topics (lorsqu'on est connecté)

Et aux fonctionnalités sophistiquées suivantes :
8. "Dans une version plus sophistiquée, on pourrait utiliser un topic (auquel tous les clients souscrivent) permettant de diffuser l'information quant à l'existance d'un nouvel hashtag."
9. "Dans une version plus sophistiquée, il faudrait trouver un moyen pour que les topics soient non seulement persistants (même si le serveur tweeter est éteint), mais les messages qu'ils contiennent également et qu'un abonné à un tel topic puisse également être notifié des messages publiés alors qu'il n'était pas connecté."


Aspects non développés
-------------------------------
Tous les aspects ont été développés. 


Organisation du code
-------------------------------
Nous avons organisé le code de la manière suivante :

###Un projet pour le server RMI
Le server RMI gère l'authentification et la création de hashtags

###Un projet pour le client
Pour créer un compte et se connecter et se déconnecter, le client fait appel au server RMI (methode de l'interface).
Le client possède un Publisher pour écrire et un subscriber pour recevoir et s'abonner.
Pour créer un nouvel hashtag, le client passe par le server RMI qui lui va enregistrer le tag auprès du serveur Apache.
	

Difficultés rencontrées
-------------------------------
L'organisation du code
La sécurisation des connexions



