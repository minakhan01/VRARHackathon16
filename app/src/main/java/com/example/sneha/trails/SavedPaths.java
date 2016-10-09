package com.example.sneha.trails;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by lucascassiano on 10/8/16.
 */

public class SavedPaths {
   public static List<List<LocationData>> paths = new List<List<LocationData>>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<List<LocationData>> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(List<LocationData> locationDatas) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends List<LocationData>> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends List<LocationData>> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public List<LocationData> get(int index) {
            return null;
        }

        @Override
        public List<LocationData> set(int index, List<LocationData> element) {
            return null;
        }

        @Override
        public void add(int index, List<LocationData> element) {

        }

        @Override
        public List<LocationData> remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<List<LocationData>> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<List<LocationData>> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<List<LocationData>> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

}
