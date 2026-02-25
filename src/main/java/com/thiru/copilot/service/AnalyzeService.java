package com.thiru.copilot.service;

import com.thiru.copilot.model.AnalyzeResponse;
import com.thiru.copilot.util.PdfUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyzeService {

    private final GenAIService genAIService;

    public AnalyzeService(GenAIService genAIService) {
        this.genAIService = genAIService;
    }

    private static final List<String> MASTER_SKILLS = Arrays.asList(
            "java", "spring boot", "microservices",
            "docker", "sql", "git", "rest"
    );

    public AnalyzeResponse analyze(MultipartFile file, String jdText) {

        if (jdText == null || jdText.trim().isEmpty()) {
            throw new RuntimeException("Job Description cannot be empty.");
        }

        try {
            String resumeText = PdfUtil.extractText(file).toLowerCase();
            jdText = jdText.toLowerCase();

            List<String> jdSkills = MASTER_SKILLS.stream()
                    .filter(jdText::contains)
                    .collect(Collectors.toList());

            if (jdSkills.isEmpty()) {
                return new AnalyzeResponse(
                        "No specific technical skills detected in the job description. Please provide a more detailed JD.",
                        0.0,
                        List.of(),
                        List.of(),
                        "Please include specific technologies like Java, Python, SQL, etc. for better analysis."
                );
            }

            List<String> matchedSkills = jdSkills.stream()
                    .filter(resumeText::contains)
                    .collect(Collectors.toList());

            List<String> missingSkills = jdSkills.stream()
                    .filter(skill -> !resumeText.contains(skill))
                    .collect(Collectors.toList());

            double matchScore =
                    ((double) matchedSkills.size() / jdSkills.size()) * 100;

            matchScore = Math.round(matchScore * 100.0) / 100.0;

            // 🔥 AI PROMPTS
            String roadmapPrompt =
                    "You are a senior career mentor. " +
                            "A fresher candidate is applying for a Java Backend Developer role. " +
                            "The candidate already knows: " + matchedSkills + ". " +
                            "The candidate is missing: " + missingSkills + ". " +
                            "Generate a practical 30-day improvement roadmap. " +
                            "Structure it week-wise. " +
                            "Include daily focus areas, mini-project ideas, and resources to learn. " +
                            "Keep it realistic for a beginner. " +
                            "Keep response clear and structured.";

            String interviewPrompt =
                    "You are a technical interviewer for a product-based company. " +
                            "Generate 10 interview questions for a fresher Java Backend Developer. " +
                            "Include: " +
                            "1. Core Java questions, " +
                            "2. Spring Boot questions, " +
                            "3. One question from each missing skill: " + missingSkills + ", " +
                            "4. 3 behavioral/HR questions. " +
                            "For each question, also give a short ideal answer guideline.";

            String roadmap = genAIService.generateContent(roadmapPrompt);
            String interview = genAIService.generateContent(interviewPrompt);

            AnalyzeResponse response = new AnalyzeResponse();
            response.setMatchScore(matchScore);
            response.setMatchedSkills(matchedSkills);
            response.setMissingSkills(missingSkills);
            response.setRoadmap(roadmap);
            response.setInterviewQuestions(interview);

            return response;

        } catch (IOException e) {
            throw new RuntimeException("Error processing file.");
        }
    }
}