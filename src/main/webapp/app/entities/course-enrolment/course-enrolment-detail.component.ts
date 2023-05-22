import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseEnrolment } from 'app/shared/model/course-enrolment.model';

@Component({
  selector: 'jhi-course-enrolment-detail',
  templateUrl: './course-enrolment-detail.component.html',
})
export class CourseEnrolmentDetailComponent implements OnInit {
  courseEnrolment: ICourseEnrolment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseEnrolment }) => (this.courseEnrolment = courseEnrolment));
  }

  previousState(): void {
    window.history.back();
  }
}
