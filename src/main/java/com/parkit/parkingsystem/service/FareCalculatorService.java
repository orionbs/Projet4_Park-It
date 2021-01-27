package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public final static double REDUCTION = 0.05;

    /**
     * Permet de calculer avec la réduction si le isCurrentUser est true
     *
     * @param ticket
     * @param isRecurrentUser
     */
    public void calculateFare(Ticket ticket, boolean isRecurrentUser) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inMilliseconds = ticket.getInTime().getTime();
        long outMilliseconds = ticket.getOutTime().getTime();

        long diffInMilliseconds = outMilliseconds - inMilliseconds;

        float durationInHours = TimeUnit.MINUTES.convert(diffInMilliseconds, TimeUnit.MILLISECONDS) / 60f;
        if (durationInHours <= 0.5f) {
            ticket.setPrice(0);
            return;
        }
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                final double discountResult = durationInHours * Fare.CAR_RATE_PER_HOUR * (isRecurrentUser ? (1 - REDUCTION) : 1);
                ticket.setPrice(discountResult);
                break;
            }
            case BIKE: {
                final double discountResult = durationInHours * Fare.BIKE_RATE_PER_HOUR * (isRecurrentUser ? (1 - REDUCTION) : 1);
                ticket.setPrice(discountResult);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    /**
     * Permet de calculer sans réduction
     *
     * @param ticket
     */
    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }
}