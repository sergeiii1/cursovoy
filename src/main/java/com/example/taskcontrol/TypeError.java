package com.example.taskcontrol;

public enum TypeError {
    CRITICAL,
    ERROR,
    WARNING;

    @Override
    public String toString() {
        return switch (this){
            case ERROR -> "ошибка";
            case WARNING -> "предупреждение";
            case CRITICAL -> "критическая ошибка";
        };
    }
    public int toInt(){
        return switch (this){
            case WARNING -> 0;
            case ERROR -> 1;
            case CRITICAL -> 2;
        };
    }
    public static TypeError fromInt(int error){
        return switch (error){
            case 0 -> WARNING;
            case 2 -> CRITICAL;
            default -> ERROR;
        };
    }
}
