package ourTwitter;


import javax.jms.JMSException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, JMSException {
        System.out.println("Bonjour, je me connecte � Twitter ! #Awesome");
        
        System.setProperty("java.security.policy","file:"+System.getProperty("user.dir")+"/java.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        
		ITwitter twitter = (ITwitter) Naming.lookup("rmi://" + InetAddress.getLocalHost().getHostAddress() + "/twitter");
        if(twitter.createAccount("Nana", "jaimelescookies"))
        	System.out.println("Création du compte Nana OK");
        else {
        	System.out.println("Compte déjà existant");
        }
        System.out.println("j'essaie de me connecter, et la réponse est : "+twitter.connect("Nana","jaimelescookies"));

        Subscriber mySub = new Subscriber();
        mySub.configurer();

        Publisher myPub = new Publisher();
        myPub.configurer();

        /*
        Alors, là en gros j'ai juste copié collé le code corrigé du tp...
        Il y a une méthode configurerPub et configurerSouscripteur dans Subscriber & Publisher, qui sont donc
        à mon avis ce qu'on doit sortir de ces classes pour rendre l'affaire interactive
        En ayant lancé le MOM apacheMQ of course ^^
        Et sinon, donc pour moi, l'idéal c'est que le côté serveur serve juste à se connecter, & si on a le temps,
        pour la persistence des messages, qu'il soit lui-même subscriber de tous les topics qui existent ?
        Je pars à la plage là :/ mais demain jsuis bien dispo dès le matin :D
        Dis moi ce que t'en penses, je suis joignable de toute façon, avec mon téléphone connecté :p
         */
    }
}

