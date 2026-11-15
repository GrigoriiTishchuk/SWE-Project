package com.edujournal.model;

/**
 * Data transfer object representing grade distribution statistics.
 * Stores percentage or count values for each grade category.
 */
public class GradeDistributionDTO {

    private double excellent;
    private double veryGood;
    private double good;
    private double satisfactory;
    private double sufficient;
    private double fail;

    /**
     * Creates a grade distribution DTO.
     *
     * @param excellent excellent grade value
     * @param veryGood very good grade value
     * @param good good grade value
     * @param satisfactory satisfactory grade value
     * @param sufficient sufficient grade value
     * @param fail fail grade value
     */
    public GradeDistributionDTO(
            double excellent,
            double veryGood,
            double good,
            double satisfactory,
            double sufficient,
            double fail
    ) {
        this.excellent = excellent;
        this.veryGood = veryGood;
        this.good = good;
        this.satisfactory = satisfactory;
        this.sufficient = sufficient;
        this.fail = fail;
    }

    /**
     * Creates an empty grade distribution DTO.
     */
    public GradeDistributionDTO() {
    }

    public double getExcellent() {
        return excellent;
    }

    public void setExcellent(double excellent) {
        this.excellent = excellent;
    }

    public double getVeryGood() {
        return veryGood;
    }

    public void setVeryGood(double veryGood) {
        this.veryGood = veryGood;
    }

    public double getGood() {
        return good;
    }

    public void setGood(double good) {
        this.good = good;
    }

    public double getSatisfactory() {
        return satisfactory;
    }

    public void setSatisfactory(double satisfactory) {
        this.satisfactory = satisfactory;
    }

    public double getSufficient() {
        return sufficient;
    }

    public void setSufficient(double sufficient) {
        this.sufficient = sufficient;
    }

    public double getFail() {
        return fail;
    }

    public void setFail(double fail) {
        this.fail = fail;
    }
}
