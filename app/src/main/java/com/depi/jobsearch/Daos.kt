package com.depi.jobsearch

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface UserProfileDao {
    @Upsert
    suspend fun upsert(userProfile: UserProfile)

    @Delete
    suspend fun delete(userProfile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    suspend fun getUserProfile(id: Long) : UserProfile?
}

@Dao
interface WorkExperienceDao {
    @Upsert
    suspend fun upsert(workExperience: WorkExperience)

    @Delete
    suspend fun delete(workExperience: WorkExperience)

    @Query("SELECT * FROM work_experience WHERE userId = :userId")
    suspend fun getWorkExperience(userId: Long) : List<WorkExperience>
}

@Dao
interface EducationDao {
    @Upsert
    suspend fun upsert(education: Education)

    @Delete
    suspend fun delete(education: Education)

    @Query("SELECT * FROM education WHERE userId = :userId")
    suspend fun getEducation(userId: Long) : List<Education>
}

@Dao
interface SkillsDao {
    @Upsert
    suspend fun upsert(skill: Skill)

    @Delete
    suspend fun delete(skill: Skill)

    @Query("SELECT * FROM skills WHERE userId = :userId")
    suspend fun getSkills(userId: Long) : List<Skill>
}