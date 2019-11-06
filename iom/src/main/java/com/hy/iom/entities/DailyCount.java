package com.hy.iom.entities;

public class DailyCount {
        private String name;
        private int value;

        public DailyCount(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return "DailyCount{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }