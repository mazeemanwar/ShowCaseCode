package com.tracker.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a DCVehicle from Parse database.
 *
 * @author Muhammad Azeem Anwar
 */

public class DCVehicle implements Serializable {
    private final static long serialVersionUID = -707272717274747L;

    private String id;
    private String make;
    private String model;
    private String regdate;
    private String colour;
    private String body;
    private String fuel;
    private String derivative;

    private String transmission;
    private String taxBand;

    private String dateMOT;
    private String dateRoadtax;
    private String service;

    public String getLastService() {
        return lastService;
    }


    public void setLastService(String lastService) {
        this.lastService = lastService;
    }

    private String lastService;
    private String registration;
    private String insurance;
    private String cc;
    private String bhp;
    private String checkDate;
    private String vehicleCover;
    private int doorCount;

    public int getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(int doorCount) {
        this.doorCount = doorCount;
    }

    public String getVehicleCover() {
        return vehicleCover;
    }

    public void setVehicleCover(String vehicleCover) {
        this.vehicleCover = vehicleCover;
    }

    public String getYearRFL() {
        return yearRFL;
    }

    public void setYearRFL(String yearRFL) {
        this.yearRFL = yearRFL;
    }

    public String getSixMonthRFL() {
        return sixMonthRFL;
    }

    public void setSixMonthRFL(String sixMonthRFL) {
        this.sixMonthRFL = sixMonthRFL;
    }

    public String getComMPG() {
        return comMPG;
    }

    public void setComMPG(String comMPG) {
        this.comMPG = comMPG;
    }

    public String getExturbMPG() {
        return exturbMPG;
    }

    public void setExturbMPG(String exturbMPG) {
        this.exturbMPG = exturbMPG;
    }

    private String yearRFL;
    private String sixMonthRFL;
    private String comMPG;
    private String exturbMPG;
    private String co2Value;
    private String gearBox;

    public String getGearBox() {
        return gearBox;
    }

    public void setGearBox(String gearBox) {
        this.gearBox = gearBox;
    }

    public String getCo2Value() {
        return co2Value;
    }

    public void setCo2Value(String co2Value) {
        this.co2Value = co2Value;
    }

    private int co2;
    private int alertsCount = 4;
    private int nServiceHistoryItems;

    private float quotedMPG;
    private float knownMPG;
    private float annualTax;
    private float MOTCost;
    private float engineSize;
    private float monthlyFinance;

    private long currentMileage;
    private long annualMileage;

    private Map<String, String> infodump = new HashMap<String, String>();

    private byte[] photoSrc;

    private boolean photo = false;
    private boolean current = false;
    private String tempInsuranceDate;

    // Getters and Setters
    // ======================================
    public byte[] getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(byte[] photoSrc) {
        this.photoSrc = photoSrc;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getRegistration() {
        return registration;
    }

    public int getAlertsCount() {
        return alertsCount;
    }

    public String getTempInsuranceDate() {
        return tempInsuranceDate;
    }

    public void setTempInsuranceDate(String tempInsuranceDate) {
        this.tempInsuranceDate = tempInsuranceDate;
    }

    public void setAlertsCount(int count) {
        this.alertsCount = count;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Map<String, String> getInfodump() {
        return infodump;
    }

    public void setInfodump(Map<String, String> infodump) {
        this.infodump = infodump;
    }

    public String getDateMOT() {
        return dateMOT;
    }

    public void setDateMOT(Date mOT) {
        if (mOT != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            this.dateMOT = format.format(mOT);
        }
    }

    public String getService() {
        return service;
    }

    public void setService(Date service) {
        if (service != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            this.service = format.format(service);
        }
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setMOT(String mot) {
        this.dateMOT = mot;
    }

    public Date getMOTDate() {
        if (this.dateMOT != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = format.parse(this.dateMOT);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getDateString(String dateText) {
        String str = dateText.replace("-", "/");
        String formatedDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date d = format.parse(str);
            SimpleDateFormat serverFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ssZ");
            formatedDate = serverFormat.format(d);
            return serverFormat.format(d);
        } catch (ParseException e) {
            System.out.println("printing date Exception ==> " + e.toString());
            e.printStackTrace();

        }
        return formatedDate;
    }

    public Date getFormatedDate(String dateString) {
        if (dateString != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = format.parse(dateString);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Date getRoadTaxDate() {
        if (this.dateRoadtax != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = format.parse(this.dateRoadtax);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Date getServiceDate() {
        if (this.service != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = format.parse(this.service);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getRoadtax() {
        return dateRoadtax;
    }

    public void setRoadtax(String roadtax) {
        this.dateRoadtax = roadtax;
    }

    public void setRoadtax(Date roadtax) {
        if (roadtax != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            this.dateRoadtax = format.format(roadtax);
        }

    }

    public String getLastCheckDate() {
        return checkDate;
    }

    public void setLastCheck(String date) {
        this.checkDate = date;
    }

    public void setLastCheckDAte(Date checkDate) {
        if (checkDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            this.checkDate = format.format(checkDate);
        }
    }

    public long getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(long mileage) {
        this.currentMileage = mileage;
    }

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getDerivative() {
        return derivative;
    }

    public void setDerivative(String derivative) {
        this.derivative = derivative;
    }

    // set cc value
    public String getCC() {
        return cc;
    }

    public void setCC(String cc) {
        this.cc = cc;
    }

    // bhp
    public String getBhp() {
        return bhp;
    }

    public void setBhp(String bhp) {
        this.bhp = bhp;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Object getAddValuesByOrder(int order) {
        switch (order) {
            case 0:
                return getMake();
            case 1:
                return getModel();
            case 2:
                return getDerivative();
            case 3:
                return getCC();
            case 4:
                return getBhp();
            case 5:
                return getBody();
            case 6:
                return getGearBox();
            case 7:
                return getFuel();
            case 8:
                return getExturbMPG();
            case 9:
                return getComMPG();
            case 10:
                return getSixMonthRFL();
            case 11:
                return getYearRFL();
            case 12:
                return getCo2Value();

            default:
                return null;
        }
    }

    public Object getDetailsByOrder(int order) {
        Object res = null;

        switch (order) {
            case 0:
                res = getMake();
                break;
            case 1:
                res = getModel();
                break;
            case 2:
                res = getDerivative();
                break;
            case 3:
                res = getCurrentMileage();
                break;
            case 4:
                res = null;
                break;
            default:
                return res;
        }
        return res;
    }

    public Object getDocumentationByOrder(int order) {
        Object res = null;
        switch (order) {
            case 0:
                res = getRoadtax();
                break;
            case 1:
                res = getDateMOT();
                break;
            case 2:
                res = getService();
                break;
            case 3:
                res = getLastService();
                break;
            default:
                return res;
        }
        return res;
    }

    public Object getStatisticsByOrder(int order) {
        Object res = null;

        switch (order) {
            case 0:
                res = co2 + " grams";
                break;
            case 1:
                res = taxBand;
                break;
            case 2:
                res = engineSize + " L";
                break;
            case 3:
                res = fuel;
                break;
            default:
                return res;
        }

        return res;
    }

    public void setMoreDetails(int order, String value) {
        switch (order) {
            case 0:
                setMOT(value);
                break;
            case 1:
                setRoadtax(value);
                break;
            case 2:
                setCurrentMileage(Long.valueOf(value));
                break;
            default:
                break;
        }
    }

    public void setDocumentations(int order, String value) {
        switch (order) {
            case 0:
                setRoadtax(value);
                break;
            case 1:
                setMOT(value);
                break;
            case 2:
                setService(value);
                break;
            case 3:
                setLastService(value);
                break;
            default:
                break;
        }
    }

    public float getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(float engineSize) {
        this.engineSize = engineSize;
    }

    public String getTaxBand() {
        return taxBand;
    }

    public void setTaxBand(String taxBand) {
        this.taxBand = taxBand;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(Date insurance) {
        if (insurance != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            this.insurance = format.format(insurance);
        }
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public int getServiceHistory() {
        return nServiceHistoryItems;
    }

    public void setServiceHistory(int serviceHistory) {
        this.nServiceHistoryItems = serviceHistory;
    }

    public long getAnnualMileage() {
        return annualMileage;
    }

    public void setAnnualMileage(long annualMileage) {
        this.annualMileage = annualMileage;
    }

    public float getQuotedMPG() {
        return quotedMPG;
    }

    public void setQuotedMPG(float quotedMPG) {
        this.quotedMPG = quotedMPG;
    }

    public float getAnnualTax() {
        return annualTax;
    }

    public void setAnnualTax(float annualTax) {
        this.annualTax = annualTax;
    }

    public float getMOTCost() {
        return MOTCost;
    }

    public void setMOTCost(float mOTCost) {
        MOTCost = mOTCost;
    }

    public float getMonthlyFinance() {
        return monthlyFinance;
    }

    public void setMonthlyFinance(float monthlyFinance) {
        this.monthlyFinance = monthlyFinance;
    }

    public float getKnownMPG() {
        return knownMPG;
    }

    public void setKnownMPG(float knownMPG) {
        this.knownMPG = knownMPG;
    }
}
