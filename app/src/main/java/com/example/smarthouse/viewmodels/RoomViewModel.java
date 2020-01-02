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

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RoomViewModel extends AuthViewModel {


    Repository mRepository;

    Context mAppContext;

    ObservableField<String> mHouseId;
    ObservableField<String> mChosenRoomId;
    ObservableField<String> mDrawerQueryText;

    Observable<String> mHouseIdObservable;
    Observable<String> mChosenRoomIdObservable;
    Observable<String> mDrawerQueryTextObservable;

    Observable<List<RoomInfo>> mRoomInfoListObservable;



    List<String> mRoomInfoIdChosenOrder;


    @Inject
    RoomViewModel(Repository repository, SharedPreferencesRepository shearedPrefrencesRepository,Context appContext) {
        super(repository, shearedPrefrencesRepository);
        this.mRepository = repository;
        this.mAppContext = appContext;

        mHouseId = new ObservableField<>();
        mChosenRoomId = new ObservableField<>();
        mDrawerQueryText = new ObservableField<>();


        mDrawerQueryTextObservable = MakeObservable.makeObservebleForString(mDrawerQueryText);

        mHouseIdObservable = MakeObservable.makeObservebleForString(mHouseId).cacheWithInitialCapacity(1);

        mChosenRoomIdObservable = MakeObservable.makeObservebleForString(mChosenRoomId).cacheWithInitialCapacity(1);

        mRoomInfoListObservable = mHouseIdObservable.switchMap((houseIdObservable) -> repository.getHousesRooms(houseIdObservable).toObservable());

        mRoomInfoIdChosenOrder = new ArrayList<>();

    }

    public Observable<String> getChosenRoomNameObservable() {
        return Observable.combineLatest(mChosenRoomIdObservable, mRoomInfoListObservable,(roomId, roomInfoList) -> {
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
        return Observable.combineLatest(mRoomInfoListObservable, mDrawerQueryTextObservable, (roomInfoList, queryText) -> {
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
         return mChosenRoomIdObservable
                    .switchMap((roomInfoId) -> mRepository.getRoomDetails(roomInfoId).toObservable())
                    .map((room) -> {
                        List<Options> options = new ArrayList<>();
                        ArrayList<Comparable> comparableList = new ArrayList<>();
                        if(room.getDoorList().size() !=0)
                        {
                            for(Door door : room.getDoorList())
                            {
                                comparableList.add(door.clone());
                            }
                            options.add(new Options(mAppContext.getString(R.string.doorOption),(List<Comparable>) comparableList.clone()));
                            comparableList.clear();
                        }
                        if(room.getLightsList().size() != 0)
                        {
                            for(Light light : room.getLightsList())
                            {
                                comparableList.add(light.clone());
                            }
                            options.add(new Options(mAppContext.getString(R.string.lightOption),(List<Comparable>) comparableList.clone()));
                            comparableList.clear();
                        }
                        if(room.getTemperatureList().size() != 0)
                        {
                            for(Temperature temperature : room.getTemperatureList())
                            {
                                comparableList.add(temperature.clone());
                            }
                            options.add(new Options(mAppContext.getString(R.string.temperatureOption),(List<Comparable>) comparableList.clone()));
                            comparableList.clear();
                        }
                        return options;
                    });
    }

    public Observable<Boolean> getHasUserChosenRoomOnceObservable() {
        return mChosenRoomIdObservable.take(1).map((roomInfo -> true));
    }

    public Observable<String> getmChosenRoomIdObservable() {
        return mChosenRoomIdObservable;
    }

    public void setmHouseId(String mHouseId) {
        this.mHouseId.set(mHouseId);
    }

    public void setmChosenRoomId(String chosenRoomInfoId) {

        addRoomIdToBackList(chosenRoomInfoId);
        this.mChosenRoomId.set(chosenRoomInfoId);
    }


    public void setmDrawerQueryText(String mDrawerQueryText) {
        this.mDrawerQueryText.set(mDrawerQueryText);
    }

    public String getmHouseId() {
        return mHouseId.get();
    }

    public String getmChosenRoomId() {
        return mChosenRoomId.get();
    }


    public List<String> getmRoomInfoIdChosenOrder() {
        return mRoomInfoIdChosenOrder;
    }

    public void putRoomInfoIdChosenOrder(String roomInfoIdChosenOrder) {
        this.mRoomInfoIdChosenOrder.add(roomInfoIdChosenOrder);
    }

    public Observable<Integer> getRoomIdPositionForBackNavigation() {
         return mRoomInfoListObservable
                 .take(1)
                 .map(
                 (roomInfoList) -> {
                     if (roomInfoList.isEmpty() || roomInfoList == null || mRoomInfoIdChosenOrder.isEmpty()) {
                         return new Integer(-1);
                     }

                     for (int i = mRoomInfoIdChosenOrder.size() -1; i >= 0; i--) {
                         for (int j = 0; j < roomInfoList.size(); j++) {
                                if(roomInfoList.get(j).getRoomId().equals(mRoomInfoIdChosenOrder.get(i))){
                                    return new Integer(i-1);
                             }
                         }
                     }
                     return new Integer(-1);
                 });
    }

    public void setUpRoomIdFromBackNavigation(Integer roomIdIndex) {
         int i = mRoomInfoIdChosenOrder.size()-1;
         for(;i>roomIdIndex;i--)
         {
             mRoomInfoIdChosenOrder.remove(i);
         }
         mChosenRoomId.set(mRoomInfoIdChosenOrder.get(i));
    }


    private void  addRoomIdToBackList(String roomId) {
        if(mRoomInfoIdChosenOrder.size()!=0)
        {
            if(!mRoomInfoIdChosenOrder.get(mRoomInfoIdChosenOrder.size()-1).equals(roomId)) {
                for (int i = 0; i < mRoomInfoIdChosenOrder.size() - 1; i++) {
                    if (roomId.equals(mRoomInfoIdChosenOrder.get(i))) {
                        mRoomInfoIdChosenOrder.remove(i);
                    }
                }
                mRoomInfoIdChosenOrder.add(roomId);
            }
        }
        else {
            mRoomInfoIdChosenOrder.add(roomId);
        }
    }


    public Completable changeDoorLocked(boolean locked,Door door) {
        String roomId = mRoomInfoIdChosenOrder.get(mRoomInfoIdChosenOrder.size() -1);
        return mRepository.changeDoorLock(roomId,door.getDoorId(),locked);
    }

    public Completable changeLightOn(boolean turnOn,Light light){
        String roomId = mRoomInfoIdChosenOrder.get(mRoomInfoIdChosenOrder.size() -1);
        return mRepository.changeLight(roomId,light.getLightId(),turnOn);
    }

    public Completable changeRoomTemeperature(int value,Temperature temperature) {
        String roomId = mRoomInfoIdChosenOrder.get(mRoomInfoIdChosenOrder.size() -1);
        return mRepository.changeTargetedTemperature(roomId,temperature.getTemperatureId(),value);
    }






}
