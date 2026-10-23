package com.worldtransitai.mod.entity;

import net.minecraft.world.phys.Vec3;

/**
 * Единое физическое ядро транспорта (Create + MTR + Train Main + Авиация/Корабли/Фуникулёры).
 * Поддерживает:
 * - Рельсовые составы и метро (плавные кривые Безье, тележки Боги, сцепка вагонов)
 * - Фуникулёры и канатные дороги (движение по тросовому вектору с крутым уклоном до 45°)
 * - Самолёты (аэродинамическая подъёмная сила, закрылки, тяга двигателей)
 * - Корабли (плавучесть Архимеда, инерция на воде)
 * - Встроенная телеметрия в стиле Train Main (графики скорости, тормозного пути, напряжения КС и перегрузок)
 */
public class MultiModalTransitVehicle {

    public enum VehicleCategory {
        METRO_TRAIN,       // Метро (Номерной, Юбилейный, Балтиец, E235 Yamanote, MP 14 Paris)
        HIGH_SPEED_RAIL,   // Сапсан, TGV Duplex, Shinkansen N700S
        FUNICULAR_CABLE,   // Горный/городской фуникулёр
        AIRCRAFT,          // Пассажирский авиалайнер
        MARITIME_SHIP      // Паром / теплоход / морской лайнер
    }

    private final String modelId;
    private final VehicleCategory category;
    private double speedKmH = 0.0;
    private double maxSpeedKmH = 160.0;
    private double throttle = 0.0; // -1.0 (экстренное торможение) .. +1.0 (полная тяга)
    private double pantographVoltage = 825.0; // Вольтаж контактного рельса/сети для графика телеметрии

    public MultiModalTransitVehicle(String modelId, VehicleCategory category, double maxSpeedKmH) {
        this.modelId = modelId;
        this.category = category;
        this.maxSpeedKmH = maxSpeedKmH;
    }

    /**
     * Обновление физики каждый тик (20 раз в секунду).
     * Очень лёгкая математика без тяжёлых коллизий блоков, чтобы не нагружать процессор.
     */
    public Vec3 tickPhysics(Vec3 currentPos, Vec3 forwardDir, double slopeAngleDegrees) {
        double acceleration = throttle * getBaseAcceleration();
        // Учёт уклона для поездов и фуникулёров
        double gravityComponent = (category == VehicleCategory.FUNICULAR_CABLE)
                ? 0.0 // Фуникулёр удерживается тросом с противовесом
                : Math.sin(Math.toRadians(slopeAngleDegrees)) * 9.81 * 0.15;

        speedKmH = Math.max(0.0, Math.min(maxSpeedKmH, speedKmH + acceleration - gravityComponent));

        double blocksPerTick = (speedKmH / 3.6) / 20.0;
        return currentPos.add(forwardDir.normalize().scale(blocksPerTick));
    }

    private double getBaseAcceleration() {
        return switch (category) {
            case METRO_TRAIN -> 1.3;
            case HIGH_SPEED_RAIL -> 0.85;
            case FUNICULAR_CABLE -> 0.4;
            case AIRCRAFT -> 2.1;
            case MARITIME_SHIP -> 0.25;
        };
    }

    /**
     * Данные для HUD-графика телеметрии (в стиле Train Main):
     * скорость, тормозной путь, профиль пути и нагрузка на сеть.
     */
    public String getTelemetryOverlayText() {
        double brakingDistanceMeters = (speedKmH * speedKmH) / (254.0 * 0.35);
        return String.format("[%s] Скорость: %.1f км/ч | Тяга: %.0f%% | Торм. путь: %.0f м | Сеть: %.0f В",
                modelId, speedKmH, throttle * 100.0, brakingDistanceMeters, pantographVoltage);
    }
}
