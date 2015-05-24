# MiniTwitter
# Projet - Application Répartie

Garance Vallat
Anaïs Marongiu
SI4


Pour faire tourner le projet
-------------------------------
1. Lancer le server Apache 
	./activemq start
2. Lancer le server RMI (classe Server)
3. Lancer le client 1 (classe Client1)
4. Lancer le client 2 juste derriere (classe Client2)
	

Fonctionnalités
-------------------------------
Notre projet répond aux fonctionnalités de base suivantes :
1. Créer un compte MiniTwitter avec un login et un password
2. Se connecter/déconnecter
3. Créer un nouveau hashtag (lorsqu'on est connecté)
4. S'abonner auprés d'un hashtag (lorsqu'on est connecté)
5. Ecrire sur un hashtag (lorsqu'on est connecté)
6. Lire les messages du topic (lorsqu'on est connecté)
7. Voir la liste des topics (lorsqu'on est connecté)

Et aux fonctionnalités sophistiquées suivantes :
8. "Dans une version plus sophistiquée, on pourrait utiliser un topic (auquel tous les clients souscrivent) permettant de diffuser l'information quant à l'existance d'un nouvel hashtag." 
9. "Dans une version plus sophistiquée, il faudrait trouver un moyen pour que les topics soient non seulement persistants (même si le serveur tweeter est éteint) et qu'un abonné à un tel topic puisse également être notifié des messages publiés alors qu'il n'était pas connecté."


Aspects non développés
-------------------------------
Tous les aspects ont été développé sauf la persistence des messages des topics. 


Organisation du code
-------------------------------
Nous avons organisé le code de la manière suivante :

###Un projet pour le server RMI
Le server RMI gère l'authentification (connexion, déconnexion)
et la création de hashtags 
et les abonnements (avec en début de connexion l'abonnement au topic spécial pour être notifié de nouveau hashtag)

###Un projet pour le client
Le client appele les methodes distantes du server RMI pour l'authentification, les créations de hashtags et abonnements.
Le client possède un Publisher pour écrire et un subscriber pour recevoir.


Difficultés rencontrées
-------------------------------
L'organisation du code
La sécurisation des connexions

