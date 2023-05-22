import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { AnswerItemUpdateComponent } from 'app/entities/answer-item/answer-item-update.component';
import { AnswerItemService } from 'app/entities/answer-item/answer-item.service';
import { AnswerItem } from 'app/shared/model/answer-item.model';

describe('Component Tests', () => {
  describe('AnswerItem Management Update Component', () => {
    let comp: AnswerItemUpdateComponent;
    let fixture: ComponentFixture<AnswerItemUpdateComponent>;
    let service: AnswerItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [AnswerItemUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AnswerItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnswerItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AnswerItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AnswerItem(123);
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
        const entity = new AnswerItem();
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
