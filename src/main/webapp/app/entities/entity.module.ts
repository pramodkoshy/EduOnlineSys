import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'teacher',
        loadChildren: () => import('./teacher/teacher.module').then(m => m.EduOnlineTeacherModule),
      },
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.EduOnlineStudentModule),
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.EduOnlineCourseModule),
      },
      {
        path: 'exam',
        loadChildren: () => import('./exam/exam.module').then(m => m.EduOnlineExamModule),
      },
      {
        path: 'question',
        loadChildren: () => import('./question/question.module').then(m => m.EduOnlineQuestionModule),
      },
      {
        path: 'question-item',
        loadChildren: () => import('./question-item/question-item.module').then(m => m.EduOnlineQuestionItemModule),
      },
      {
        path: 'answer-item',
        loadChildren: () => import('./answer-item/answer-item.module').then(m => m.EduOnlineAnswerItemModule),
      },
      {
        path: 'course-module',
        loadChildren: () => import('./course-module/course-module.module').then(m => m.EduOnlineCourseModuleModule),
      },
      {
        path: 'course-unit',
        loadChildren: () => import('./course-unit/course-unit.module').then(m => m.EduOnlineCourseUnitModule),
      },
      {
        path: 'course-learning-objects',
        loadChildren: () =>
          import('./course-learning-objects/course-learning-objects.module').then(m => m.EduOnlineCourseLearningObjectsModule),
      },
      {
        path: 'course-enrolment',
        loadChildren: () => import('./course-enrolment/course-enrolment.module').then(m => m.EduOnlineCourseEnrolmentModule),
      },
      {
        path: 'exam-registration',
        loadChildren: () => import('./exam-registration/exam-registration.module').then(m => m.EduOnlineExamRegistrationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EduOnlineEntityModule {}
