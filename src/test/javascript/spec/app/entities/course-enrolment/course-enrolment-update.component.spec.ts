import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseEnrolmentUpdateComponent } from 'app/entities/course-enrolment/course-enrolment-update.component';
import { CourseEnrolmentService } from 'app/entities/course-enrolment/course-enrolment.service';
import { CourseEnrolment } from 'app/shared/model/course-enrolment.model';

describe('Component Tests', () => {
  describe('CourseEnrolment Management Update Component', () => {
    let comp: CourseEnrolmentUpdateComponent;
    let fixture: ComponentFixture<CourseEnrolmentUpdateComponent>;
    let service: CourseEnrolmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseEnrolmentUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CourseEnrolmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseEnrolmentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseEnrolmentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseEnrolment(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseEnrolment();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
