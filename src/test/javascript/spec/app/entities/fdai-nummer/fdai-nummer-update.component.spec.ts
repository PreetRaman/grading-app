/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { FdaiNummerUpdateComponent } from 'app/entities/fdai-nummer/fdai-nummer-update.component';
import { FdaiNummerService } from 'app/entities/fdai-nummer/fdai-nummer.service';
import { FdaiNummer } from 'app/shared/model/fdai-nummer.model';

describe('Component Tests', () => {
    describe('FdaiNummer Management Update Component', () => {
        let comp: FdaiNummerUpdateComponent;
        let fixture: ComponentFixture<FdaiNummerUpdateComponent>;
        let service: FdaiNummerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [FdaiNummerUpdateComponent]
            })
                .overrideTemplate(FdaiNummerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FdaiNummerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FdaiNummerService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FdaiNummer(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.fdaiNummer = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FdaiNummer();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.fdaiNummer = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
