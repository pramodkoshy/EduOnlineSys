import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICourseModule, CourseModule } from 'app/shared/model/course-module.model';
import { CourseModuleService } from './course-module.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

@Component({
  selector: 'jhi-course-module-update',
  templateUrl: './course-module-update.component.html',
})
export class CourseModuleUpdateComponent implements OnInit {
  isSaving = false;
  courses: ICourse[] = [];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    description: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    courseId: [],
  });

  constructor(
    protected courseModuleService: CourseModuleService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseModule }) => {
      this.updateForm(courseModule);

      this.courseService.query().subscribe((res: HttpResponse<ICourse[]>) => (this.courses = res.body || []));
    });
  }

  updateForm(courseModule: ICourseModule): void {
    this.editForm.patchValue({
      id: courseModule.id,
      name: courseModule.name,
      description: courseModule.description,
      startDate: courseModule.startDate,
      endDate: courseModule.endDate,
      courseId: courseModule.courseId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseModule = this.createFromForm();
    if (courseModule.id !== undefined) {
      this.subscribeToSaveResponse(this.courseModuleService.update(courseModule));
    } else {
      this.subscribeToSaveResponse(this.courseModuleService.create(courseModule));
    }
  }

  private createFromForm(): ICourseModule {
    return {
      ...new CourseModule(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      courseId: this.editForm.get(['courseId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseModule>>): void {
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

  trackById(index: number, item: ICourse): any {
    return item.id;
  }
}
