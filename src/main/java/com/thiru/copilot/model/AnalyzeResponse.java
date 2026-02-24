package com.thiru.copilot.model;

import java.util.List;

public class AnalyzeResponse {

    private double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String roadmap;
    private String interviewQuestions;

    public double getMatchScore() { return matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public String getRoadmap() { return roadmap; }
    public void setRoadmap(String roadmap) { this.roadmap = roadmap; }

    public String getInterviewQuestions() { return interviewQuestions; }
    public void setInterviewQuestions(String interviewQuestions) { this.interviewQuestions = interviewQuestions; }
}
