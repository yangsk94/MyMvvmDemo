package com.event;

import android.support.annotation.NonNull;
import com.utils.Logger;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;

    public static synchronized RxBus get() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject= PublishSubject.create();
        subjectList.add(subject);
        Logger.INSTANCE.e(TAG, "[register]subjectMapper: " + subjectMapper);
        return subject;
    }

    @SuppressWarnings("unchecked")
    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
            }
        }
        Logger.INSTANCE.e(TAG, "[register]subjectMapper: " + subjectMapper);
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);

        if (subjectList != null && !subjectList.isEmpty()) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
        Logger.INSTANCE.e(TAG, "[register]subjectMapper: " + subjectMapper);
    }
}