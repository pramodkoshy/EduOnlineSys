import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICourseLearningObjects } from 'app/shared/model/course-learning-objects.model';

@Component({
  selector: 'jhi-course-learning-objects-detail',
  templateUrl: './course-learning-objects-detail.component.html',
})
export class CourseLearningObjectsDetailComponent implements OnInit {
  courseLearningObjects: ICourseLearningObjects | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseLearningObjects }) => (this.courseLearningObjects = courseLearningObjects));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
