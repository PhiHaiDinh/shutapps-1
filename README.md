# ShutApps

**english version below!**

Die mobile Anwendung "ShutApps" ist während meines Studiums "Medieninformatik" in dem Modul Praxisprojekt in Zusammenarbeit mit Vu Phi Hai Dinh entstanden,
das als Vorprojekt zur Bachelorarbeit gilt. Die Idee aus diesem Projekt bildet die Grundidee meiner Bachelorarbeit. Während Phi Hai größenteils für die konzeptionellen
Modellierungen zuständig war, war der Großteil meiner Aufgabe die technische Umsetzung und Implementierung der Android-App.

Das Ziel dieses Projekts ist zu beweisen, ob es technisch machbar ist, mit einer mobilen Anwendung die Smartphone-Nutzung auf den Geräten von Freunden einzuschränken.
Hier soll ich mit der App, bestimmte Apps auf den Smartphones von Freunden blockieren können, wenn sie in der Nähe sind. Voraussetzung ist, dass alle Beteiligten die App bewusst nutzen.

Quellcode: [Android-App](https://github.com/ducle07/shutapps/tree/master/ControllingApps)

## Architektur & verwendete Technologien
![Architekturdiagramm](https://github.com/ducle07/shutapps/blob/master/Architektur.png)

* **Android Studio** als Entwicklungsumgebung
* auf **Firebase** basierte Architektur, kein Server vorhanden
  * **Firebase Realtime Database** für Echtzeit-Datenhaltung
  * **Firebase Storage** zur Speicherung von Bildern (Profilbilder, App-Icons)
  * **Firebase Authentication** zur Authentifizierung in der Anwendung
* Client als mobiler Client in **Android**
  * **Accessibility-Service** zur Erkennung der Vordergrund-App
  * **NotificationListenerService** zur Unterdrückung der ankommenden Benachrichtigungen
* Anbindung von Facebook mit der **Facebook Graph API**
* Client zu Client Kommunikation vorher mit **Google Nearby Messages API**, jetzt mit **Android Beacon Library**
                                                               
--------------------
--------------------

# English version

The mobile application "ShutApps" was developed during my study "media computer science" in the module "practical project" in collaberation with Vu Phi Hai Dinh. The module is considered as the preliminary project for the bachelor thesis. The idea of my bachelor thesis is based on the idea, the problems and the results of this practical project. While Phi Hai was largely responsible for the conceptual modeling, most of my tasks were the technical implementation of the Android application.

The aim of this project is to prove whether it is technically possible to use a mobile application to limit the smartphone usage on friends' devices. With the mobile application I am supposed to be able to block certain apps on friends' smartphones when they are nearby. The prerequisite is that all participants are consciously using the mobile application.

Source Code: [Android-App](https://github.com/ducle07/shutapps/tree/master/ControllingApps)

## Architecture and used technologies
![architecture](https://github.com/ducle07/shutapps/blob/master/Architektur.png)

* **Android Studio** as development environment
* on **Firebase** based architecture, no server in use
  * **Firebase Realtime Database** for real-time data management
  * **Firebase Storage** for storing pictures (profile pictures, app icons)
  * **Firebase Authentication** for authentication in the application
* Client as a mobile client in **Android**
  * **Accessbility Service** to detect the foreground app
  * **NotificationListenerService** to suppress incoming notifications
* Connecting Facebook with the **Facebook Graph API**
* Client to client communication, before with **Google Nearby Messages API**, now with **Android Beacon Library**
