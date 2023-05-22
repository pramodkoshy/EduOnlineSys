import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { QuestionItemUpdateComponent } from 'app/entities/question-item/question-item-update.component';
import { QuestionItemService } from 'app/entities/question-item/question-item.service';
import { QuestionItem } from 'app/shared/model/question-item.model';

describe('Component Tests', () => {
  describe('QuestionItem Management Update Component', () => {
    let comp: QuestionItemUpdateComponent;
    let fixture: ComponentFixture<QuestionItemUpdateComponent>;
    let service: QuestionItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [QuestionItemUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(QuestionItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuestionItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(QuestionItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new QuestionItem(123);
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
        const entity = new QuestionItem();
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
