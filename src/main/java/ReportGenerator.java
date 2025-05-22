//package com.company;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class ReportGenerator {
    private HotelManager hotelManager;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public ReportGenerator(HotelManager hotelManager) {
        this.hotelManager = hotelManager;
    }
    
    public Map<String, Double> generateDailyRevenue(Date date) {
        double roomRevenue = 0.0;
        double foodRevenue = 0.0;
        
        for (Billing billing : hotelManager.getBillings().values()) {
            if (isSameDay(billing.getBillingDate(), date)) {
                roomRevenue += billing.getRoomCharges();
                foodRevenue += billing.getFoodCharges();
            }
        }
        
        Map<String, Double> revenue = new HashMap<>();
        revenue.put("roomRevenue", roomRevenue);
        revenue.put("foodRevenue", foodRevenue);
        revenue.put("totalRevenue", roomRevenue + foodRevenue);
        return revenue;
    }
    
    public Map<String, Double> generateMonthlyRevenue(int month, int year) {
        Map<String, Double> revenue = new HashMap<>();
        double roomRevenue = 0.0;
        double foodRevenue = 0.0;
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        System.out.println("Generating revenue report for: " + dateFormat.format(cal.getTime()));
        
        List<Billing> monthlyBills = hotelManager.getBillings().values().stream()
            .filter(bill -> {
                cal.setTime(bill.getBillingDate());
                return cal.get(Calendar.MONTH) == month && 
                       cal.get(Calendar.YEAR) == year;
            })
            .collect(Collectors.toList());
        
        for (Billing bill : monthlyBills) {
            roomRevenue += bill.getRoomCharges();
            foodRevenue += bill.getFoodCharges();
        }
        
        revenue.put("roomRevenue", roomRevenue);
        revenue.put("foodRevenue", foodRevenue);
        revenue.put("totalRevenue", roomRevenue + foodRevenue);
        return revenue;
    }
    
    public Map<String, Object> generateOccupancyReport() {
        Map<String, Object> report = new HashMap<>();
        List<Room> rooms = hotelManager.getRooms();
        
        int totalRooms = rooms.size();
        int occupiedRooms = (int) rooms.stream()
            .filter(Room::isOccupied)
            .count();
        int availableRooms = totalRooms - occupiedRooms;
        double occupancyRate = (totalRooms > 0) ? 
            ((double) occupiedRooms / totalRooms) * 100 : 0;
        
        Map<String, Integer> roomTypeOccupancy = new HashMap<>();
        for (Room room : rooms) {
            if (room.isOccupied()) {
                String roomType = room.getRoomType();
                roomTypeOccupancy.merge(roomType, 1, Integer::sum);
            }
        }
        
        report.put("totalRooms", totalRooms);
        report.put("occupiedRooms", occupiedRooms);
        report.put("availableRooms", availableRooms);
        report.put("occupancyRate", occupancyRate);
        report.put("roomTypeOccupancy", roomTypeOccupancy);
        
        return report;
    }
    
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
               cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
} 