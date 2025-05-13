package ru.ani.web.services;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Component;
import ru.ani.web.interfaces.PointCounterMBean;
import ru.ani.web.models.Point;

@ManagedResource(objectName = "ru.ani.web:type=PointCounter")
@Component
public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMBean {
    private int totalPoints = 0;
    private int invalidPoints = 0;
    private long sequenceNumber = 1;

    @Override
    @ManagedAttribute
    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    @ManagedAttribute
    public int getInvalidPoints() {
        return invalidPoints;
    }

    // Вызывается при сохранении точки (в контроллере)
    public void incrementCounters(Point point) {
        totalPoints++;
        if (isOutOfDisplayArea(point)) {
            invalidPoints++;
            sendNotification(new Notification(
                    "PointOutOfBounds",
                    this,
                    sequenceNumber++,
                    "Точка (" + point.getX() + ", " + point.getY() + ") вне области"
            ));
        }
    }

    // Проверка на выход за пределы отображаемой области (X: -5..5, Y: -5..5)
    private boolean isOutOfDisplayArea(Point point) {
        return point.getX() < -5 || point.getX() > 5 || point.getY() < -5 || point.getY() > 5;
    }

    @ManagedOperation
    public void sendNotification() {
        // Пример ручной отправки уведомления
    }
}