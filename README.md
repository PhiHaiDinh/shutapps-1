# ShutApps

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
                                                               
