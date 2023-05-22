import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseUnitUpdateComponent } from 'app/entities/course-unit/course-unit-update.component';
import { CourseUnitService } from 'app/entities/course-unit/course-unit.service';
import { CourseUnit } from 'app/shared/model/course-unit.model';

describe('Component Tests', () => {
  describe('CourseUnit Management Update Component', () => {
    let comp: CourseUnitUpdateComponent;
    let fixture: ComponentFixture<CourseUnitUpdateComponent>;
    let service: CourseUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseUnitUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CourseUnitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseUnitUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseUnitService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseUnit(123);
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
        const entity = new CourseUnit();
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
