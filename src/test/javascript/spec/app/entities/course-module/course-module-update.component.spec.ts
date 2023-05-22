import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseModuleUpdateComponent } from 'app/entities/course-module/course-module-update.component';
import { CourseModuleService } from 'app/entities/course-module/course-module.service';
import { CourseModule } from 'app/shared/model/course-module.model';

describe('Component Tests', () => {
  describe('CourseModule Management Update Component', () => {
    let comp: CourseModuleUpdateComponent;
    let fixture: ComponentFixture<CourseModuleUpdateComponent>;
    let service: CourseModuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseModuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CourseModuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseModuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseModuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CourseModule(123);
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
        const entity = new CourseModule();
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
