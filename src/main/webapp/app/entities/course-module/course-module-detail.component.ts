import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseModule } from 'app/shared/model/course-module.model';

@Component({
  selector: 'jhi-course-module-detail',
  templateUrl: './course-module-detail.component.html',
})
export class CourseModuleDetailComponent implements OnInit {
  courseModule: ICourseModule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseModule }) => (this.courseModule = courseModule));
  }

  previousState(): void {
    window.history.back();
  }
}
