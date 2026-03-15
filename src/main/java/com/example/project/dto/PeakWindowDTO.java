package com.example.project.dto;

import lombok.Data;

@Data
public class PeakWindowDTO {
    private String peakTimeStart;
    private String peakTimeEnd;


    public String getPeakTimeStart() {
        return peakTimeStart;
    }

    public void setPeakTimeStart(String peakTimeStart) {
        this.peakTimeStart = peakTimeStart;
    }

    public String getPeakTimeEnd() {
        return peakTimeEnd;
    }

    public void setPeakTimeEnd(String peakTimeEnd) {
        this.peakTimeEnd = peakTimeEnd;
    }
}
