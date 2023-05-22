import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICourseUnit, CourseUnit } from 'app/shared/model/course-unit.model';
import { CourseUnitService } from './course-unit.service';
import { ICourseModule } from 'app/shared/model/course-module.model';
import { CourseModuleService } from 'app/entities/course-module/course-module.service';

@Component({
  selector: 'jhi-course-unit-update',
  templateUrl: './course-unit-update.component.html',
})
export class CourseUnitUpdateComponent implements OnInit {
  isSaving = false;
  coursemodules: ICourseModule[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    description: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    courseModuleId: [],
  });

  constructor(
    protected courseUnitService: CourseUnitService,
    protected courseModuleService: CourseModuleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseUnit }) => {
      this.updateForm(courseUnit);

      this.courseModuleService.query().subscribe((res: HttpResponse<ICourseModule[]>) => (this.coursemodules = res.body || []));
    });
  }

  updateForm(courseUnit: ICourseUnit): void {
    this.editForm.patchValue({
      id: courseUnit.id,
      name: courseUnit.name,
      description: courseUnit.description,
      courseModuleId: courseUnit.courseModuleId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseUnit = this.createFromForm();
    if (courseUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.courseUnitService.update(courseUnit));
    } else {
      this.subscribeToSaveResponse(this.courseUnitService.create(courseUnit));
    }
  }

  private createFromForm(): ICourseUnit {
    return {
      ...new CourseUnit(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      courseModuleId: this.editForm.get(['courseModuleId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseUnit>>): void {
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

  trackById(index: number, item: ICourseModule): any {
    return item.id;
  }
}
