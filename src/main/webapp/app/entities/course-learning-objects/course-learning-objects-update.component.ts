import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICourseLearningObjects, CourseLearningObjects } from 'app/shared/model/course-learning-objects.model';
import { CourseLearningObjectsService } from './course-learning-objects.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ICourseUnit } from 'app/shared/model/course-unit.model';
import { CourseUnitService } from 'app/entities/course-unit/course-unit.service';

@Component({
  selector: 'jhi-course-learning-objects-update',
  templateUrl: './course-learning-objects-update.component.html',
})
export class CourseLearningObjectsUpdateComponent implements OnInit {
  isSaving = false;
  courseunits: ICourseUnit[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    type: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    text: [null, [Validators.required]],
    url: [null, [Validators.required, Validators.minLength(5), Validators.maxLength(1000)]],
    courseUnitId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected courseLearningObjectsService: CourseLearningObjectsService,
    protected courseUnitService: CourseUnitService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseLearningObjects }) => {
      this.updateForm(courseLearningObjects);

      this.courseUnitService.query().subscribe((res: HttpResponse<ICourseUnit[]>) => (this.courseunits = res.body || []));
    });
  }

  updateForm(courseLearningObjects: ICourseLearningObjects): void {
    this.editForm.patchValue({
      id: courseLearningObjects.id,
      name: courseLearningObjects.name,
      type: courseLearningObjects.type,
      text: courseLearningObjects.text,
      url: courseLearningObjects.url,
      courseUnitId: courseLearningObjects.courseUnitId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('eduOnlineApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseLearningObjects = this.createFromForm();
    if (courseLearningObjects.id !== undefined) {
      this.subscribeToSaveResponse(this.courseLearningObjectsService.update(courseLearningObjects));
    } else {
      this.subscribeToSaveResponse(this.courseLearningObjectsService.create(courseLearningObjects));
    }
  }

  private createFromForm(): ICourseLearningObjects {
    return {
      ...new CourseLearningObjects(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      text: this.editForm.get(['text'])!.value,
      url: this.editForm.get(['url'])!.value,
      courseUnitId: this.editForm.get(['courseUnitId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseLearningObjects>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICourseUnit): any {
    return item.id;
  }
}
