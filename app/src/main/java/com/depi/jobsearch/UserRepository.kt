package com.depi.jobsearch

class UserRepository(
    private val userProfileDao: UserProfileDao,
    private val workExperienceDao: WorkExperienceDao,
    private val educationDao: EducationDao,
    private val skillsDao: SkillsDao,
) {
    suspend fun upsertUserProfile(userProfile: UserProfile) = userProfileDao.upsert(userProfile)
    suspend fun getUserProfile(id: Long) = userProfileDao.getUserProfile(id)

    suspend fun upsertWorkExperience(workExperience: WorkExperience)
        = workExperienceDao.upsert(workExperience)
    suspend fun getWorkEcperience(userId: Long) = workExperienceDao.getWorkExperience(userId)

    suspend fun upsertEducation(education: Education) = educationDao.upsert(education)
    suspend fun getEducation(userId: Long) = educationDao.getEducation(userId)

    suspend fun upsertSkill(skill: Skill) = skillsDao.upsert(skill)
    suspend fun getSkills(userId: Long) = skillsDao.getSkills(userId)


    suspend fun syncWorkExperienceWithFirestore(userProfile: UserProfile) {

    }

    suspend fun syncWorkExperienceWithFirestore(workExperience: WorkExperience) {
        // Add Firestore sync logic here
    }

    suspend fun syncEducationWithFirestore(education: Education) {
        // Add Firestore sync logic here
    }

    suspend fun syncSkillsWithFirestore(skill: Skill) {
        // Add Firestore sync logic here
    }
}