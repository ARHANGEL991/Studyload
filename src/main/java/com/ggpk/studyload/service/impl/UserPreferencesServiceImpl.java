package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.service.UserPreferencesService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.prefs.Preferences;

import static java.util.prefs.Preferences.userRoot;

@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {

    private Preferences userPref;

    @PostConstruct
    private void init() {
        userPref = userRoot().node("studyLoad");

    }

    public String getTeacherReportFolderPath() {
        return userPref.get("teacherReportPath", System.getProperty("user.dir"));
    }

    public String getGroupReportFolderPath() {
        return userPref.get("groupReportPath", System.getProperty("user.dir"));
    }

    public String getYearReportFolderPath() {
        return userPref.get("yearReportPath", System.getProperty("user.dir"));
    }

    public void setTeacherReportPath(String path) {
        userPref.put("teacherReportPath", path);
    }

    public void setGroupReportPath(String path) {
        userPref.put("groupReportPath", path);
    }

    public void setYearReportPath(String path) {
        userPref.put("yearReportPath", path);
    }

    public String getTeacherReportTemplateFilePath() {
        return userPref.get("teacherReportTemplateFilePath", System.getProperty("user.dir"));
    }

    public String getGroupReportTemplateFilePath() {
        return userPref.get("groupReportTemplateFilePath", System.getProperty("user.dir"));
    }


    public String getYearReportTemplateFilePath() {
        return userPref.get("yearReportTemplateFilePath", System.getProperty("user.dir"));
    }

    public void setTeacherReportTemplateFilePath(String filePath) {
        userPref.put("teacherReportTemplateFilePath", filePath);
    }

    public void setGroupReportTemplateFilePath(String filePath) {
        userPref.put("groupReportTemplateFilePath", filePath);
    }

    public void setYearReportTemplateFilePath(String filePath) {
        userPref.put("yearReportTemplateFilePath", filePath);
    }


    public void setImportPath(String path) {
        userPref.put("importPath", path);
    }

    public String getImportPath() {
        return userPref.get("importPath", "");
    }
}
