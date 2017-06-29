package com.mooo.sestus.indoor_locator.data;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

public class RemoteFloorPlanRepository implements FloorPlanRepository {
    private static RemoteFloorPlanRepository INSTANCE;
    private final WebSocket ws;
    private final Gson gson;
    private LoadFloorPlansCallback loadFloorPlansCallback;
    private LoadFingerPrintedLocationsCallback loadFingerPrintedLocationsCallback;
    private LoadPointOfInterestsCallback loadPointOfInterestsCallback;
    private Map<Integer, FloorPlan> floorPlans;
    private Map<Integer, FingerPrintedLocation> fingerPrintedLocations;
    private Map<Integer, PointOfInterest> pointsOfInterest;
    private List<FingerPrint> newFingerprints = new ArrayList<>();
    private AddFingerPrintedLocationCallback addFingerPrintedLocationCallback;
    private AddPoiCallback addPoiCallback;
    private LocateCallback locateCallback;

    private RemoteFloorPlanRepository() {
        Request request = new Request.Builder().url("ws://192.168.1.2:8000/realtime").build();
        DjangoChannelListener listener = new DjangoChannelListener();
        ws = new OkHttpClient().newWebSocket(request, listener);
        gson = new Gson();
    }

    @Override
    public FloorPlan addFloorPlan(String name, Bitmap bitmap, SaveFloorPlanCallback callback) {
        return null;
    }

    @Override
    public void getFloorPlans(@NonNull LoadFloorPlansCallback callback) {
        if (floorPlans != null)
            callback.onFloorPlansLoaded(Collections.unmodifiableCollection(floorPlans.values()));
        else {
            this.loadFloorPlansCallback = callback;
            ws.send(gson.toJson(JsonData.createFor(WebSocketCommand.GET_FLOOR_PLANS)));
        }
    }

    @Override
    public FloorPlan getFloorPlanById(int floorPlanId) {
        return floorPlans.get(floorPlanId);
    }


    @Override
    public boolean containsFloorPlan(String name) {
        return false;
    }

    @Override
    public void addFingerPrintedLocation(int floorPlanId, PointF pointF, AddFingerPrintedLocationCallback callback) {
        this.addFingerPrintedLocationCallback = callback;
        ws.send(gson.toJson(AddFingerPrintedLocationJson.createFor(floorPlanId, pointF.x, pointF.y)));
    }


    @Override
    public void getFingerPrintedLocations(int floorPlanId, LoadFingerPrintedLocationsCallback callback) {
        this.loadFingerPrintedLocationsCallback = callback;
        ws.send(gson.toJson(GetFingerPrintedLocationJson.createFor(floorPlanId)));
    }

    @Override
    public FingerPrintedLocation getFingerPrintedLocationById(int id) {
        return fingerPrintedLocations.get(id);
    }

    @Override
    public void addPoiToFloorPlan(int floorPlanId, PointF pointF, AddPoiCallback callback) {
        this.addPoiCallback = callback;
        ws.send(gson.toJson(AddPoiJson.createFor(floorPlanId, pointF.x, pointF.y)));
    }


    @Override
    public void getFloorPlanPois(int floorPlanId, LoadPointOfInterestsCallback callback) {
        if (pointsOfInterest != null)
            callback.onPoisLoaded(Collections.unmodifiableCollection(pointsOfInterest.values()));
        else {
            this.loadPointOfInterestsCallback = callback;
            ws.send(gson.toJson(GetPoisJson.createFor(floorPlanId)));
        }
    }

    @Override
    public PointOfInterest getPointOfInterestById(int id) {
        return pointsOfInterest.get(id);
    }

    @Override
    public void addFingerPrint(FingerPrint fingerPrint) {
        newFingerprints.add(fingerPrint);
    }

    @Override
    public void saveFingerPrints() {
        ws.send(gson.toJson(AddFingerPrintJson.createFor(Collections.unmodifiableList(newFingerprints))));
        newFingerprints.clear();
    }

    @Override
    public void getClosestPoint(int floorPlanId, FingerPrint fingerPrint, LocateCallback callback) {
        ws.send(gson.toJson(LocateJson.createFor(floorPlanId, fingerPrint)));
        this.locateCallback = callback;
    }

    void onMessageReceived(String text) {
        if (this.loadFloorPlansCallback != null) {
            Type listType = new TypeToken<ArrayList<FloorPlan>>(){}.getType();
            List<FloorPlan> floorPlen = gson.fromJson(text, listType);
            loadFloorPlansCallback.onFloorPlansLoaded(floorPlen);
            floorPlans = new HashMap<>();
            fingerPrintedLocations = new HashMap<>();
            for (FloorPlan floorPlan : floorPlen) {
                floorPlans.put(floorPlan.getId(), floorPlan);
            }
            loadFloorPlansCallback = null;
        }
        else if (this.addFingerPrintedLocationCallback != null) {
            FingerPrintedLocation location = gson.fromJson(text, FingerPrintedLocation.class);
            fingerPrintedLocations.put(location.getId(), location);
            addFingerPrintedLocationCallback.onLocationAdded(location);
            addFingerPrintedLocationCallback = null;
        }
        else if (this.addPoiCallback != null) {
            PointOfInterest poi = gson.fromJson(text, PointOfInterest.class);
            pointsOfInterest.put(poi.getId(), poi);
            addPoiCallback.onPoiAdded(poi);
            addPoiCallback = null;
        }
        else if (this.loadFingerPrintedLocationsCallback != null) {
            Type listType = new TypeToken<ArrayList<FingerPrintedLocation>>(){}.getType();
            List<FingerPrintedLocation> locations = gson.fromJson(text, listType);
            loadFingerPrintedLocationsCallback.onLocationsLoaded(locations);
            for (FingerPrintedLocation location : locations) {
                fingerPrintedLocations.put(location.getId(), location);
            }
            loadFingerPrintedLocationsCallback = null;
        }
        else if (this.loadPointOfInterestsCallback != null) {
            pointsOfInterest = new HashMap<>();
            Type listType = new TypeToken<ArrayList<PointOfInterest>>(){}.getType();
            List<PointOfInterest> pois = gson.fromJson(text, listType);
            loadPointOfInterestsCallback.onPoisLoaded(pois);
            for (PointOfInterest poi : pois) {
                pointsOfInterest.put(poi.getId(), poi);
            }
            loadPointOfInterestsCallback = null;
        }
        else if (this.locateCallback != null) {
            ResolvedLocation location = gson.fromJson(text, ResolvedLocation.class);
            locateCallback.onLocateResult(location);
            locateCallback = null;
        }
    }


    public static RemoteFloorPlanRepository getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RemoteFloorPlanRepository();
        return INSTANCE;
    }

    public void onSocketClosing(WebSocket webSocket, int code, String reason) {
        Log.v("Websocket", String.format("onSocketClosing %d %s", code, reason));

    }

    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.v("Websocket", String.format("onFailure %s", t.getMessage()));
        t.printStackTrace();
    }
}

enum WebSocketCommand {
    GET_FLOOR_PLANS, ADD_POI, GET_LOCATIONS, GET_POIS, ADD_LOCATION, ADD_FINGERPRINTS, LOCATE
}

class JsonData {
    private WebSocketCommand command;

    static JsonData createFor(WebSocketCommand command) {
        JsonData json = new JsonData();
        json.command = command;
        return json;
    }
}

class AddFingerPrintedLocationJson {
    private WebSocketCommand command;
    private FingerPrintedLocation location;

    static AddFingerPrintedLocationJson createFor(int floorPlanId, float xCoord, float yCoord) {
        AddFingerPrintedLocationJson json = new AddFingerPrintedLocationJson();
        json.command = WebSocketCommand.ADD_LOCATION;
        json.location = new FingerPrintedLocation(floorPlanId, xCoord, yCoord);
        return json;
    }
}

class AddPoiJson {
    private WebSocketCommand command;
    private PointOfInterest poi;

    static AddPoiJson createFor(int floorPlanId, float xCoord, float yCoord) {
        AddPoiJson json = new AddPoiJson();
        json.command = WebSocketCommand.ADD_POI;
        json.poi = new PointOfInterest(floorPlanId, xCoord, yCoord);
        return json;
    }
}

class GetFingerPrintedLocationJson {
    private WebSocketCommand command;
    private int floorPlanId;

    static GetFingerPrintedLocationJson createFor(int floorPlanId) {
        GetFingerPrintedLocationJson json = new GetFingerPrintedLocationJson();
        json.command = WebSocketCommand.GET_LOCATIONS;
        json.floorPlanId = floorPlanId;
        return json;
    }
}

class GetPoisJson {
    private WebSocketCommand command;
    private int floorPlanId;

    static GetPoisJson createFor(int floorPlanId) {
        GetPoisJson json = new GetPoisJson();
        json.command = WebSocketCommand.GET_POIS;
        json.floorPlanId = floorPlanId;
        return json;
    }
}

class AddFingerPrintJson {
    private WebSocketCommand command;
    private List<FingerPrint> fingerPrintList;

    static AddFingerPrintJson createFor(List<FingerPrint> fingerPrintList) {
        AddFingerPrintJson json = new AddFingerPrintJson();
        json.command = WebSocketCommand.ADD_FINGERPRINTS;
        json.fingerPrintList = fingerPrintList;
        return json;
    }
}

class LocateJson {
    private WebSocketCommand command;
    private FingerPrint fingerPrint;
    private int floorPlanId;

    static LocateJson createFor(int floorPlanId, FingerPrint fingerPrint) {
        LocateJson json = new LocateJson();
        json.command = WebSocketCommand.LOCATE;
        json.fingerPrint = fingerPrint;
        json.floorPlanId = floorPlanId;
        return json;
    }
}
