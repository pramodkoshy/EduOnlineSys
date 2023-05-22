import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseUnit } from 'app/shared/model/course-unit.model';

@Component({
  selector: 'jhi-course-unit-detail',
  templateUrl: './course-unit-detail.component.html',
})
export class CourseUnitDetailComponent implements OnInit {
  courseUnit: ICourseUnit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseUnit }) => (this.courseUnit = courseUnit));
  }

  previousState(): void {
    window.history.back();
  }
}
