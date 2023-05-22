import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { ExamRegistrationUpdateComponent } from 'app/entities/exam-registration/exam-registration-update.component';
import { ExamRegistrationService } from 'app/entities/exam-registration/exam-registration.service';
import { ExamRegistration } from 'app/shared/model/exam-registration.model';

describe('Component Tests', () => {
  describe('ExamRegistration Management Update Component', () => {
    let comp: ExamRegistrationUpdateComponent;
    let fixture: ComponentFixture<ExamRegistrationUpdateComponent>;
    let service: ExamRegistrationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [ExamRegistrationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ExamRegistrationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExamRegistrationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExamRegistrationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExamRegistration(123);
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
        const entity = new ExamRegistration();
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
