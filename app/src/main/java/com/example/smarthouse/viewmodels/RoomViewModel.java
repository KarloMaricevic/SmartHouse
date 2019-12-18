package com.example.smarthouse.viewmodels;

import android.content.Context;

import androidx.databinding.ObservableField;

import com.example.smarthouse.R;
import com.example.smarthouse.adapters.RoomFragment.RoomDetails.expandableGroup.Options;
import com.example.smarthouse.data.roomData.Door;
import com.example.smarthouse.data.roomData.Light;
import com.example.smarthouse.data.roomData.RoomInfo;
import com.example.smarthouse.data.roomData.Temperature;
import com.example.smarthouse.repositorys.Repository;
import com.example.smarthouse.repositorys.SharedPreferencesRepository;
import com.example.smarthouse.utils.MakeObservable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class RoomViewModel extends AuthViewModel {


    Repository repository;

    Context appContext;

    ObservableField<String> houseId;
    ObservableField<String> chosenRoomId;
    ObservableField<String> drawerQueryText;

    Observable<String> houseIdObservable;
    Observable<String> chosenRoomIdObservable;
    Observable<String> drawerQueryTextObservable;

    Observable<List<RoomInfo>> roomInfoListObservable;



    List<String> roomInfoIdChosenOrder;


    @Inject
    RoomViewModel(Repository repository, SharedPreferencesRepository shearedPrefrencesRepository,Context appContext) {
        super(repository, shearedPrefrencesRepository);
        this.repository = repository;
        this.appContext = appContext;

        houseId = new ObservableField<>();
        chosenRoomId = new ObservableField<>();
        drawerQueryText = new ObservableField<>();


        drawerQueryTextObservable = MakeObservable.makeObservebleForString(drawerQueryText);

        houseIdObservable = MakeObservable.makeObservebleForString(houseId).cacheWithInitialCapacity(1);

        chosenRoomIdObservable = MakeObservable.makeObservebleForString(chosenRoomId).cacheWithInitialCapacity(1);

        roomInfoListObservable = houseIdObservable.switchMap((houseIdObservable) -> repository.getHousesRooms(houseIdObservable).toObservable());

        roomInfoIdChosenOrder = new ArrayList<>();

    }

    public Observable<String> getChosenRoomNameObservable() {
        return Observable.combineLatest(chosenRoomIdObservable,roomInfoListObservable,(roomId, roomInfoList) -> {
                    for(RoomInfo roomInfo : roomInfoList)
                    {
                        if(roomInfo.getRoomId().equals(roomId))
                        {
                            return roomInfo.getName();
                        }
                    }
                    return "";
                });
    }

    public Observable<List<RoomInfo>> roomListFilteredObservable() {
        return Observable.combineLatest(roomInfoListObservable, drawerQueryTextObservable, (roomInfoList, queryText) -> {
            if (queryText.equals("")) {
                return roomInfoList;
            } else {
                List<RoomInfo> filteredRoomInfo = new ArrayList<>();
                for (RoomInfo roomInfo : roomInfoList) {
                    if (roomInfo.getName().toLowerCase().contains(queryText.toLowerCase())) {
                        filteredRoomInfo.add(roomInfo);
                    }
                }
                return filteredRoomInfo;
            }
        });
    }

    public Observable<List<Options>> getObservableDataForRoomDetailsAdapter() {
         return chosenRoomIdObservable
                    .switchMap((roomInfoId) -> repository.getRoomDetails(roomInfoId).toObservable())
                    .map((room) -> {
                        List<Options> options = new ArrayList<>();
                        ArrayList<Comparable> comparableList = new ArrayList<>();
                        if(room.getDoorList().size() !=0)
                        {
                            for(Door door : room.getDoorList())
                            {
                                comparableList.add(door.clone());
                            }
                            options.add(new Options(appContext.getString(R.string.doorOption),(List<Comparable>) comparableList.clone()));
                            comparableList.clear();
                        }
                        if(room.getLightsList().size() != 0)
                        {
                            for(Light light : room.getLightsList())
                            {
                                comparableList.add(light.clone());
                            }
                            options.add(new Options(appContext.getString(R.string.lightOption),(List<Comparable>) comparableList.clone()));
                            comparableList.clear();
                        }
                        if(room.getTemperatureList().size() != 0)
                        {
                            for(Temperature temperature : room.getTemperatureList())
                            {
                                comparableList.add(temperature.clone());
                            }
                            options.add(new Options(appContext.getString(R.string.temperatureOption),(List<Comparable>) comparableList.clone()));
                            comparableList.clear();
                        }
                        return options;
                    });
    }

    public Observable<Boolean> getHasUserChosenRoomOnceObservable() {
        return chosenRoomIdObservable.take(1).map((roomInfo -> true));
    }

    public Observable<String> getChosenRoomIdObservable() {
        return chosenRoomIdObservable;
    }

    public void setHouseId(String houseId) {
        this.houseId.set(houseId);
    }

    public void setChosenRoomId(String chosenRoomInfoId) {

        if(!roomInfoIdChosenOrder.get(roomInfoIdChosenOrder.size()-1).equals(chosenRoomInfoId)) {
            for (int i = 0; i < roomInfoIdChosenOrder.size() - 1; i++) {
                if (chosenRoomInfoId.equals(roomInfoIdChosenOrder.get(i))) {
                    roomInfoIdChosenOrder.remove(i);
                }
            }
            roomInfoIdChosenOrder.add(chosenRoomInfoId);
        }
        this.chosenRoomId.set(chosenRoomInfoId);
    }


    public void setDrawerQueryText(String drawerQueryText) {
        this.drawerQueryText.set(drawerQueryText);
    }

    public String getHouseId() {
        return houseId.get();
    }

    public String getChosenRoomId() {
        return chosenRoomId.get();
    }


    public List<String> getRoomInfoIdChosenOrder() {
        return roomInfoIdChosenOrder;
    }

    public void putRoomInfoIdChosenOrder(String roomInfoIdChosenOrder) {
        this.roomInfoIdChosenOrder.add(roomInfoIdChosenOrder);
    }

    public Observable<Integer> getRoomIdPositionForBackNavigation() {
         return roomInfoListObservable
                 .take(1)
                 .map(
                 (roomInfoList) -> {
                     if (roomInfoList.isEmpty() || roomInfoList == null || roomInfoIdChosenOrder.isEmpty()) {
                         return new Integer(-1);
                     }

                     for (int i = roomInfoIdChosenOrder.size() -1 ; i >= 0; i--) {
                         for (int j = 0; j < roomInfoList.size(); j++) {
                                if(roomInfoList.get(j).getRoomId().equals(roomInfoIdChosenOrder.get(i))){
                                    return new Integer(i-1);
                             }
                         }
                     }
                     return new Integer(-1);
                 });
    }

    public void setUpRoomIdFromBackNavigation(Integer roomIdIndex)
    {
         int i = roomInfoIdChosenOrder.size()-1;
         for(;i>roomIdIndex;i--)
         {
             roomInfoIdChosenOrder.remove(i);
         }
         chosenRoomId.set(roomInfoIdChosenOrder.get(i));
    }

}
