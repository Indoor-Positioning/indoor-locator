# Indoor locator

This server is the "client" of [indoor locator server](https://github.com/Indoor-Positioning/indoor-locator-server) web app.
This client communicates via a websocket to the server in order to add Point of Interests, Finger Printed locations, and Fingerprints, and retrieve the matched
location from the server.

### Architecture

This app is supposed to follow the [android MVP pattern](https://antonioleiva.com/mvp-android/) and is organized according to the [package by feature] (http://www.javapractices.com/topic/TopicAction.do?Id=205).
So basically by looking at the apps packages the features of the application can be extracted:
* selectfloorplan : This feature allows user to select a floor plan (an indoor map) to scan / locate etc.
* viewfloorplan : This feature shows the floor plan to the user along with the fingerprinted locations, the Point of interests etc.
* addfloorplan : This feature allows a user to upload a floor plan to the server.
* locate : This feature allows the user to be located. It basically sends the fingerprint of the current location and the server responds with the closest fingerprint.
* scan : This feature allows the user to scan an area (or Point of Interest) and upload this fingerprint to the server.


##### Select floor plan

This is the main activity (e.g the first screen that a user gets). In order for the app to get and display
the current floor plans (the indoor maps) a connection with the server must be established. The ip of 
the server is currently hardcoded on the [RemoteFloorPlanRepository class](https://github.com/Indoor-Positioning/indoor-locator/blob/5b08cb27ca0fdc500bc1a3db81755c63b25c4b5b/app/src/main/java/com/mooo/sestus/indoor_locator/data/RemoteFloorPlanRepository.java#L36)
,sorry for that!
 
<img src="https://user-images.githubusercontent.com/8919901/32418846-b6dbad7c-c279-11e7-998b-670fe7bf0efe.gif" width="180" height="300">

##### View floor plan

This activity gets displayed when the user selects a floor plan. The fingerprinted locations and the Point Of Interest are displayed on the map. Note
that the Point of Interests have a cross in their mark.

The user can also add new locations  (single tap) or POIs (long tap) to the map.


<p align="center">
    <img src="https://user-images.githubusercontent.com/8919901/32418847-b7026336-c279-11e7-887b-2d77f322e4d8.gif" width="180" height="300" align="left">
    <img src="https://user-images.githubusercontent.com/8919901/32418848-b727577c-c279-11e7-9135-01a303710db8.gif" width="180" height="300" align="center">
</p>


##### Scan

The user selects a location or a POI to scan, and then uploads its fingerprint to the server. 

<p align="center">
    <img src="https://user-images.githubusercontent.com/8919901/32418849-b74d1656-c279-11e7-9d3f-5d8a58e94d18.gif" width="180" height="300" align="left">
    <img src="https://user-images.githubusercontent.com/8919901/32418850-b7759d92-c279-11e7-9e90-7d3546870a35.gif" width="180" height="300" align="center">
</p>


##### Locate

The user requests to be located. A fingerprint is sent (through the websocket) and the servers responds
with the estimated location. If the location is a POI a snackbar is shown to let the user know.

<p align="center">
    <img src="https://user-images.githubusercontent.com/8919901/32418851-b7c40b44-c279-11e7-9a5c-aede496ec111.gif" width="180" height="300" align="left">
    <img src="https://user-images.githubusercontent.com/8919901/32418852-b7e9502a-c279-11e7-97b9-eecdebd9945a.gif" width="180" height="300" align="center">
</p>


### Notes - Thoughts for Future Work

- **Change the mechanism for loading the floor plan image** drawable for every floorplan must be present in the res/drawable directory. This was done
in order to avoid transferring (and serializing - deserializing imaged over the websocket).
The drawable must have the same name as the corresponding resource_name field in the floor plan 
table of the server.

- **Define proximity properly.** Currently when a server responds with a match (a finger-printed
location or a POI), the distance of this closest fingerprint is returned. The client then decides
if the closest fingerprint is indeed close, and that is currently hardcoded in [Locatorpresenter.java#L29](https://github.com/Indoor-Positioning/indoor-locator/blob/master/app/src/main/java/com/mooo/sestus/indoor_locator/locate/LocatePresenter.java#L29)
If this distance value is too big then we assume that we don't have a matching fingerprint
thus we can't determine the location.

- **Eliminate false positives in a proper way**. When two locations have similar fingerprints
we can have "false positives", meaning that the server responds with a match (i.e a matching location
and a distance which is less than the configured distance threshold) that is wrong. We try to 
eliminate these false positives using a queue (a LinkedList - see [here](https://github.com/Indoor-Positioning/indoor-locator/blob/master/app/src/main/java/com/mooo/sestus/indoor_locator/locate/LocatePresenter.java#L39)) which has the last 5
resolved locations. Majority wins, meaning that if the queue contains location A 4 times and 
location B 1 time, the location B is considered a false positive - thus discarded. This 
needs to be handled in a better way - probably on the server side.

- **Reduce the noise on the measurements properly**. We currently do averaging (5 measurements
averaged to produce a value - see [MagneticScanPresenter](https://github.com/Indoor-Positioning/indoor-locator/blob/master/app/src/main/java/com/mooo/sestus/indoor_locator/scan/MagneticScanPresenter.java)) in address to address the noise of the phone sensors. The sensors
produced by the accelerometer is a combination of Gaussian and non-Gaussian noise.

- **Come up with a model to address variance between different android phones**. Measurements
are greatly affected by the model of the android phone (e.g due to different sensors useed, different
orientation of the sensors etc). We could probably extract some parameters / come up with a model
to address this variance.

- **Replace Sqlite with a proper database**