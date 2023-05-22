import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseLearningObjectsUpdateComponent } from 'app/entities/course-learning-objects/course-learning-objects-update.component';
import { CourseLearningObjectsService } from 'app/entities/course-learning-objects/course-learning-objects.service';
import { CourseLearningObjects } from 'app/shared/model/course-learning-objects.model';

describe('Component Tests', () => {
  describe('CourseLearningObjects Management Update Component', () => {
    let comp: CourseLearningObjectsUpdateComponent;
    let fixture: ComponentFixture<CourseLearningObjectsUpdateComponent>;
    let service: CourseLearningObjectsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseLearningObjectsUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CourseLearningObjectsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseLearningObjectsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseLearningObjectsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseLearningObjects(123);
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
        const entity = new CourseLearningObjects();
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
