package by.vshkl.translate.utilities;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.translate.model.Stop;
import io.paperdb.Book;
import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DbHelper {

    private static final String BOOK_STOPS = "BOOK_STOPS";

    public static Observable<Boolean> writeStop(final String stopUrl,
                                                @Nullable final String stopName,
                                                @Nullable final String stopDirection) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                String stopId = UrlHelper.extractStopId(stopUrl);

                final Stop stop = new Stop();
                stop.setUrl(stopUrl);
                stop.setName(stopName != null ? stopName : stopId);
                stop.setDirection(stopDirection != null ? stopDirection : "");

                Book book = Paper.book(BOOK_STOPS).write(stopId, stop);

                if (book != null) {
                    emitter.onNext(true);
                } else {
                    emitter.onNext(false);
                }
            }
        });
    }

    public static Observable<Boolean> deleteStop(final String stopKey) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Paper.book(BOOK_STOPS).delete(stopKey);
                emitter.onNext(true);
            }
        });
    }

    public static Observable<List<Stop>> readStops() {
        return Observable.create(new ObservableOnSubscribe<List<Stop>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Stop>> emitter) throws Exception {
                List<Stop> stops = new ArrayList<>();
                for (String key : Paper.book(BOOK_STOPS).getAllKeys()) {
                    stops.add(Paper.book(BOOK_STOPS).read(key, new Stop()));
                }
                emitter.onNext(stops);
            }
        });
    }
}
