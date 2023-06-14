package com.example.taskcontrol;

/**
 * Класс, представляющий компьютер.
 */
public class Computer {
    /**
     * Название компьютера.
     */
    String title;

    /**
     * Идентификатор компьютера.
     */
    int id;

    /**
     * Конструктор класса Computer.
     *
     * @param computerBuilder - объект класса ComputerBuilder, содержащий параметры для создания компьютера
     */
    public Computer(ComputerBuilder computerBuilder) {
        this.title = computerBuilder.title;
        this.id = computerBuilder.id;
    }

    /**
     * Метод для получения названия компьютера.
     *
     * @return название компьютера
     */
    public String toString() {
        return title;
    }

    /**
     * Метод для создания копии текущего объекта компьютера.
     *
     * @return копия текущего объекта компьютера
     */
    public Computer clone() {
        return new Computer.ComputerBuilder(this.title + "(копия)").build();
    }

    /**
     * Внутренний класс для построения объекта класса Computer.
     */
    public static class ComputerBuilder {
        /**
         * Название компьютера.
         */
        String title;

        /**
         * Идентификатор компьютера.
         */
        int id;

        /**
         * Конструктор класса ComputerBuilder.
         *
         * @param title - название компьютера
         */
        public ComputerBuilder(String title) {
            this.title = title;
        }

        /**
         * Метод для установки идентификатора компьютера.
         *
         * @param id - идентификатор компьютера
         * @return объект класса ComputerBuilder с заданным идентификатором
         */
        public ComputerBuilder setId(int id) {
            this.id = id;
            return this;
        }

        /**
         * Метод для создания объекта класса Computer.
         *
         * @return объект класса Computer с заданными параметрами
         */
        public Computer build() {
            return new Computer(this);
        }
    }
}
