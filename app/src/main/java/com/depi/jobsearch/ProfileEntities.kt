package com.depi.jobsearch

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val aboutMe: String,
    val resumeFilename: String,
    val workExperience: List<WorkExperience> = emptyList(),
    val education: List<Education> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val languages: List<Language> = emptyList(),
    val appreciations: List<String> = emptyList()
)

@Entity(tableName = "work_experience")
data class WorkExperience(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val company: String,
    val position: String,
    val startDate: String,
    val endDate: String
)

@Entity(tableName = "education")
data class Education(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val institution: String,
    val degree: String,
    val graduationDate: String
)

@Entity(tableName = "skills")
data class Skill(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val skillName: String
)

@Entity(tableName = "languages")
data class Language(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val languageName: String,
    val languageLevel: String = ""
)